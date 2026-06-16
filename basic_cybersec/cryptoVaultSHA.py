import argparse 
import hashlib

def caesar_encrypt(text, shift):
    result = ""
    for ch in text:
        if ch.isalpha():
            base = ord('A') if ch.isupper() else ord('a')
            ch = chr((ord(ch) - base + shift) % 26 + base)
        result += ch
    return result

def caesar_decrypt(text, shift):
    result = ""
    for ch in text:
        if ch.isalpha():
            base = ord('A') if ch.isupper() else ord('a')
            ch = chr((ord(ch) - base - shift) % 26 + base)
        result += ch
    return result

parser = argparse.ArgumentParser(description="CryptoVault Phase 2 - Hash Guard")
parser.add_argument("operation", choices=["encrypt", "decrypt"], help="The operation to perform")
parser.add_argument("filename", help="The target file")
parser.add_argument("--shift", type=int, default=3, help="The shift key for the cipher (default: 3)")
parser.add_argument("--verify", action="store_true", help="Enable Hash Guard for integrity checking")

args = parser.parse_args()

try:
    with open(args.filename, "r", encoding="utf-8") as f:
        text = f.read()
except FileNotFoundError:
    print(f"Error: The file '{args.filename}' was not found.")
    exit(1)

SEPARATOR = ":::SHA256:::"

if args.operation == "encrypt":
    encrypted_text = caesar_encrypt(text, args.shift)
    
    if args.verify:
        file_hash = hashlib.sha256(text.encode('utf-8')).hexdigest()
        encrypted_text = f"{encrypted_text}{SEPARATOR}{file_hash}"
        
    print(encrypted_text)

elif args.operation == "decrypt":
    if args.verify and SEPARATOR in text:
        encrypted_content, stored_hash = text.rsplit(SEPARATOR, 1)
        decrypted_text = caesar_decrypt(encrypted_content, args.shift)
        
        recomputed_hash = hashlib.sha256(decrypted_text.encode('utf-8')).hexdigest()
        
        if recomputed_hash == stored_hash:
            print("[\033[92mSUCCESS\033[0m] Integrity verified. Hashes match.\n")
        else:
            print("[\033[91mWARNING\033[0m] Tamper detected! The file has been altered.\n")
            
        print(decrypted_text)
    else:
        if SEPARATOR in text:
            text = text.split(SEPARATOR)[0]
        print(caesar_decrypt(text, args.shift))