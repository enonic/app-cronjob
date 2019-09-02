# Simple scheduler for Enonic XP

[![Build Status](https://travis-ci.org/enonic/app-cronjob.svg?branch=master)](https://travis-ci.org/enonic/app-cronjob)
[![codecov](https://codecov.io/gh/enonic/app-cronjob/branch/master/graph/badge.svg)](https://codecov.io/gh/enonic/app-cronjob)
[![License](https://img.shields.io/github/license/enonic/app-cronjob.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This application enables simple cron-scheduling of jobs via Enonic's Cron library. The jobs are implemented in pure JavaScript and bundled into your
own application. It is not cluster safe since every job is executed in its own node. If you install the application that contains
the jobs on every node, then every node will execute the same code.

## Installing

Simply install the application from Enonic Market.

## Setting up jobs

After you have installed this application you can create your first job. Create a job in your application by adding a javascript
file inside ``/jobs`` folder. The job's entry point must be exported outside via `run` method.

myjob.js:
```js
exports.run = function() {
    log.info('Hello Job!');
};
```

The job will by default run with ``anonymous`` access-rights. So if you need to run it under a different user/role, you will have to do a context-switch 
(see documentation of [context-functions](https://developer.enonic.com/docs/xp/stable/api/lib-context)).

Next you will have to schedule the job. To schedule jobs, create a file called ``/jobs/jobs.xml`` that contains the following code:

```xml
<jobs>
  <job name="myjob" cron="* * * * *"/>
</jobs>
```

where `myjob.js` is the name of your Javascript file.

You can have multiple jobs scheduled. The ``name`` is which script to execute and ``cron`` is a cron-pattern 
(see [UINX cron pattern](https://en.wikipedia.org/wiki/Cron)).

