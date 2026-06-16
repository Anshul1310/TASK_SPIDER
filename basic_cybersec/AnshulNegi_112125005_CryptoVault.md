# CryptoVault — Write-Up

**Name:** Anshul Negi  
**Roll No:** 112125005
**Date:** 16 JUNE 2026

---

## Overview
**Caesor Lock**It works by shifting every letter in a message by a fixed number of positions in the alphabet.
For example, with a shift of 3:
A → D
B → E
C → F

The program cryptovault.py can encrypt or decrypt the data using the following command 
Encrypt

python cryptovault.py encrypt message.txt --shift 7
Decrypt

python cryptovault.py decrypt message.txt --shift 7

The argumnets are parsed via argparse module and accordingly shifting is done

## Why is Caesar Cipher Breakable?

The Caesar Cipher is very easy to break because it has only 25 possible keys. An hacker or attacker can simply try every possible shift until readable text appears. This method is called brute force attack. Since computers can test all shifts instantly, Caesar Cipher does not provide real security.


## Frequency Analysis Way
Every language has certain patterns
In English:
E is the most common letter.
In hindi
म is the most common letter
The Caesar Cipher only shifts letters without changing their frequencies. Therefore, the most common encrypted letters usually correspond to the most common English letters. By seeing these frequencies, attackers can guess the key and recover the original message.

## SHA-256
SHA-256 is a cryptographic hash function that converts data into a fixed-length string of 256 bits.

Implementation of --verify Flag

**Before encryption:**
Read the original file.
Compute its SHA-256 hash.
Store the hash inside the encrypted file.

**During decryption:**
Extract the stored hash.
Decrypt the content.
Compute a new SHA-256 hash.
Compare both hashes.

## Why Do We Need Both Encryption and Hashing?

Encryption and hashing serve different purposes.
Encryption Alone Is Not Enough
Encryption hides the contents of a file, but it does not tell us whether someone modified it.
An attacker may alter encrypted data without us noticing.
Hashing Alone Is Not Enough
A hash can verify whether data changed, but it does not hide the information.
Anyone can still read the file.
Using Both Together

**When encryption and hashing are combined:**

The data remains secret.
Any modification can be detected.
This provides both confidentiality and integrity, which are essential goals of cybersecurity.