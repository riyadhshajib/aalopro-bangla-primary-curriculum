# AaloPro repository delivery rule

When an authorized AaloPro implementation task is complete and its relevant checks have passed, synchronize the latest source before reporting final delivery:

1. Preserve user changes and do not push incomplete or failing work.
2. Never commit credentials, signing files, APKs, AABs, local settings, logs, or temporary outputs.
3. Run the relevant targeted tests and validators for the changed scope.
4. Run `tools/safe_git_sync.ps1` only after those checks pass.
5. Report the pushed commit hash and any verification limits.

The canonical private remote is `https://github.com/riyadhshajib/aalopro-bangla-primary-curriculum.git`.
