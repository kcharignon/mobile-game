#!/usr/bin/env bash
# Pose les quatre secrets de signature dans le dépôt GitHub, depuis le dossier de la clé (par défaut ~/petitsjeux-cle).
# Aucune valeur n'est affichée : les secrets passent par l'entrée standard de `gh secret set`. Voir android/PUBLICATION.md, étape 3.
set -euo pipefail

dir="${1:-$HOME/petitsjeux-cle}"
keystore="$dir/petitsjeux-publication.p12"
password_file="$dir/mot-de-passe.txt"
alias_name="${ANDROID_KEY_ALIAS:-petitsjeux}"

[ -f "$keystore" ] || { echo "Clé introuvable : $keystore (android/PUBLICATION.md, étape 1)." >&2; exit 1; }
[ -f "$password_file" ] || { echo "Mot de passe introuvable : $password_file (android/PUBLICATION.md, étape 1)." >&2; exit 1; }
command -v gh > /dev/null || { echo "La commande gh (GitHub CLI) est nécessaire." >&2; exit 1; }

base64 -w0 "$keystore" | gh secret set ANDROID_KEYSTORE_BASE64
tr -d '\n' < "$password_file" | gh secret set ANDROID_KEYSTORE_PASSWORD
tr -d '\n' < "$password_file" | gh secret set ANDROID_KEY_PASSWORD
gh secret set ANDROID_KEY_ALIAS --body "$alias_name"
echo "Secrets posés :"
gh secret list
