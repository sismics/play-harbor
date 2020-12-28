[![GitHub release](https://img.shields.io/github/release/sismics/play-harbor.svg?style=flat-square)](https://github.com/sismics/play-harbor/releases/latest)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# play-harbor plugin

This plugin adds [Harbor](https://goharbor.io/) support to Play! Framework 1 applications.

# Features

# How to use

####  Add the dependency to your `dependencies.yml` file

```
require:
    - harbor -> harbor 1.2.0

repositories:
    - sismicsNexusRaw:
        type: http
        artifact: "https://nexus.sismics.com/repository/sismics/[module]-[revision].zip"
        contains:
            - harbor -> *

```
####  Set configuration parameters

Add the following parameters to **application.conf**:

```
# Harbor configuration
# ~~~~~~~~~~~~~~~~~~~~
harbor.mock=false
harbor.url=https://harbor.example.com
harbor.user=admin
harbor.password=12345678
```
####  Use the API

```
HarborClient.get().getProjectService().createProject("my_project");
```

####  Mock the Harbor server in dev

We recommand to mock Harbor in development mode and test profile.

Use the following configuration parameter:

```
harbor.mock=true
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
