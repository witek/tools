This repository contains a collection of tools (for (Clojure) developers).

---

# gtsay

Text-to-Speech

[![bb compatible](https://raw.githubusercontent.com/babashka/babashka/master/logo/badge.svg)](https://babashka.org)

Install with [bbin](https://github.com/babashka/bbin):

```
bbin install https://raw.githubusercontent.com/witek/tools/main/gtsay.clj
```

Usage Example:

```
gtsay hello world
```

This command uses *Google Translate's Text-to-Speech*.

This project is *not* affiliated with Google or Google Cloud. Breaking upstream changes *can* occur without notice. This project is leveraging the undocumented [Google Translate](https://translate.google.com) speech functionality and is *different* from [Google Cloud Text-to-Speech](https://cloud.google.com/text-to-speech/).

---

Thanks to [Michael Borkent](https://github.com/borkdude) for [Babashka](https://babashka.org/) and for support.
