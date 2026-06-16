#!/bin/bash

targetdir="${1:-/var/www/spider-app}"
logfile="vault_sweep.log"

timestamp() {
    date '+%Y-%m-%d %H:%M:%S'
}


find "$targetdir" -name "*.sh" -type f 2>/dev/null | while read -r filepath; do
    echo "[FILE PATH]: $filepath"
    foundthreat=0

    if grep -nE '(rm\s+(-[a-zA-Z]*f[a-zA-Z]*\s+/|--no-preserve-root)|mkfs\.|shutdown|reboot|init\s+[06]|dd\s+.*of=/dev/)' "$filepath" 2>/dev/null; then
        echo ""
        echo "[WARN] $filepath"
        echo "Reason: contains destructive command"
        echo "[$(timestamp)] [WARN] $filepath contains destructive command" >> "$logfile"
        foundthreat=1
    fi

    if grep -nE '(curl|wget)\s+.*\|\s*(sh|bash|sudo\s+sh|sudo\s+bash)' "$filepath" 2>/dev/null; then
        echo ""
        echo "[WARN] $filepath"
        echo "Reason: suspicious download piped into shell"
        echo "[$(timestamp)] [WARN] $filepath contains suspicious download piped into shell" >> "$logfile"
        foundthreat=1
    fi

    if stat -c "%a" "$filepath" 2>/dev/null | grep -q "777"; then
        echo ""
        echo "[WARN] $filepath"
        echo "Reason: contains chmod 777"
        echo "[$(timestamp)] [WARN] $filepath contains chmod 777" >> "$logfile"
        foundthreat=1
    fi

    if ls -l "$filepath" 2>/dev/null | cut -c9 | grep -q "w"; then
        echo ""
        echo "[WARN] $filepath"
        echo "Reason: world-writable permission set"
        echo "[$(timestamp)] [WARN] $filepath has world-writable permission" >> "$logfile"
        foundthreat=1
    fi

    if [ "$foundthreat" -eq 1 ]; then
        echo ""
        read -p "Do you want to fix permissions for $filepath? (yes/no): " answer < /dev/tty
        if [ "$answer" = "yes" ]; then
            chmod 755 "$filepath"
            echo "[FIX] Permissions set to 755 for $filepath"
            echo "[$(timestamp)] [FIX] $filepath removed world write permission" >> "$logfile"
        else
            echo "[SKIP] No changes made to $filepath"
            echo "[$(timestamp)] [SKIP] $filepath user skipped fix" >> "$logfile"
        fi
    else
        echo "[INFO] No threats detected."
    fi

    echo ""
done

echo "STAGE 1 complete"
echo "ENV FILE CLEANING"


find "$targetdir" -name ".env*" -type f 2>/dev/null | while read -r envfile; do
    echo "[ENV FILE]: $envfile"
    sanitized="${envfile}.sanitized"
    countvalid=0
    countinvalid=0
    listrejected=""

    > "$sanitized"

    while IFS= read -r line || [ -n "$line" ]; do
        if echo "$line" | grep -qE '^\s*$|^\s*#'; then
            continue
        fi

        clean_line=$(echo "$line" | sed 's/\s*#.*//')

        if echo "$clean_line" | grep -qE '^[^=]+\s+=|=\s+'; then
            echo "     [REJECT] $line  --> Space around '='"
            countinvalid=$((countinvalid + 1))
            if [ -n "$listrejected" ]; then listrejected="$listrejected, "; fi
            listrejected="${listrejected}${clean_line}"
            continue
        fi

        key=$(echo "$clean_line" | cut -d'=' -f1)
        value=$(echo "$clean_line" | cut -d'=' -f2-)

        if echo "$key" | grep -q "^export"; then
            echo "     [REJECT] $line  --> export statement"
            countinvalid=$((countinvalid + 1))
            if [ -n "$listrejected" ]; then listrejected="$listrejected, "; fi
            listrejected="${listrejected}${clean_line}"
            continue
        fi

        if ! echo "$key" | grep -qE '^[A-Z0-9_]+$'; then
            echo "     [REJECT] $line  --> Invalid key format (only A-Z, 0-9, _ allowed)"
            countinvalid=$((countinvalid + 1))
            if [ -n "$listrejected" ]; then listrejected="$listrejected, "; fi
            listrejected="${listrejected}${clean_line}"
            continue
        fi

        if echo "$key" | grep -qiE '(PASSWORD|SECRET|TOKEN|^PATH$)'; then
            echo "     [REJECT] $line  --> Sensitive/system variable"
            countinvalid=$((countinvalid + 1))
            if [ -n "$listrejected" ]; then listrejected="$listrejected, "; fi
            listrejected="${listrejected}${clean_line}"
            continue
        fi

        if echo "$value" | grep -qE '^".*"$|^'\''.*'\''$'; then
            echo "     [REJECT] $line  --> Unnecessary quotes around value"
            countinvalid=$((countinvalid + 1))
            if [ -n "$listrejected" ]; then listrejected="$listrejected, "; fi
            listrejected="${listrejected}${clean_line}"
            continue
        fi

        echo "$clean_line" >> "$sanitized"
        countvalid=$((countvalid + 1))

    done < "$envfile"

    echo "     [RESULT] Valid: $countvalid, Invalid: $countinvalid"
    echo "     [SAVED] Sanitized file: $sanitized"
    echo "[$(timestamp)] [INFO] $envfile Valid: $countvalid, Invalid: $countinvalid" >> "$logfile"
    if [ -n "$listrejected" ]; then
        echo "[$(timestamp)] [SKIP] $envfile Rejected: $listrejected" >> "$logfile"
    fi
    echo ""
done

echo "Stage 2 complete"
echo "Writing logs"

echo "[$(timestamp)] Vault Sweep completed." >> "$logfile"