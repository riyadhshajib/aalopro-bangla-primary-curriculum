# AaloPro variant matrix

Variants share the same core source. A variant is configuration, not a copied app project.

| Concern | Dev | Tester | Demo | Production |
|---|---|---|---|---|
| Purpose | engineering | external validation | controlled presentation | Play Store users |
| Debug tools | enabled | minimal | hidden | disabled |
| Test data | allowed and labeled | fresh user state | scripted optional profile | none |
| Curriculum source | current working set | accepted test snapshot | accepted showcase snapshot | fully approved release snapshot |
| Online AI | developer configuration | tester-safe managed path | managed path if demo requires it | App Check-protected managed path |
| Analytics | debug markers | enabled, privacy-safe | enabled with demo channel | enabled, privacy-safe |
| Needle | experimental only | off unless benchmark build | off unless explicitly demonstrating research | off until all gates pass |
| Signing | debug | controlled test signing | controlled distribution signing | Play release signing |
| Distribution | local | direct tester delivery | controlled stakeholder delivery | Play Store AAB |

## Theme or concept variants

A new visual concept may change tokens, illustrations, card treatments, onboarding copy, and feature emphasis. It may not silently change curriculum truth, privacy, analytics contracts, offline/online boundaries, navigation reachability, or answer safety.

Before creating a concept variant, record:

1. audience and purpose;
2. what differs from the canonical product;
3. what is explicitly inherited unchanged;
4. success metric and expiry/review date;
5. merge, archive, or reject decision after evaluation.

