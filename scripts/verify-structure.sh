#!/usr/bin/env bash
set -euo pipefail

echo "Checking production source hygiene..."
if grep -R -nE 'System\.out|System\.err' src/main/java --include='*.java'; then
  echo "Production console output found." >&2
  exit 1
fi
if find src/main/java -type f \( -name '*.tmp' -o -name '*.bak' -o -name '*~' \) | grep -q .; then
  echo "Temporary source files found." >&2
  exit 1
fi
python3 - <<'PY'
from pathlib import Path
import re
root=Path('src/main/java')
classes={p.relative_to(root).with_suffix('').as_posix().replace('/','.') for p in root.rglob('*.java')}
for p in root.rglob('*.java'):
    for imp in re.findall(r'^import\s+([\w.]+);', p.read_text(errors='ignore'), re.M):
        if imp.startswith(('com.sms.','com.location.','com.admission.','com.StudentManagementSystemApplication')) and imp not in classes:
            raise SystemExit(f'Missing internal import: {p}: {imp}')
print('Internal imports OK')
PY
echo "Structure verification passed."
