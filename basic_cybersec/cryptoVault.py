import argparse

parser=argparse.ArgumentParser()
parser.add_argument("operation")
parser.add_argument("filename")
parser.add_argument("--shift",type=int,default=3)

args=parser.parse_args()

# print(args.operation)
# print(args.filename)
# print(args.shift)

with open(args.filename, "r") as f:
    text = f.read()

if(args.operation=="encrypt"):
    encrypted_text = caesar_encrypt(text, args.shift)
    print(encrypted_text)
else :
    decrypted_text = caesar_decrypt(text, args.shift)
    print(decrypted_text)

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
            ch = chr((ord(ch) - base + shift) % 26 + base)
        result += ch
    return result