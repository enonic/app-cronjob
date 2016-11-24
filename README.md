# Simple scheduler for Enonic XP

[![License](https://img.shields.io/github/license/enonic/app-cronjob.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This application enables a simple cron-scheduling of jobs. The jobs are implemented in pure JavaScript and bundled into your
own application. It is not cluster safe since every job is executed in it's own node. If you install the application that contains
the jobs on every node, then every node will execute the same code.


## Setting up jobs

After you have installed this application you can create your first job. Create a job in your application by adding a javascript
file inside ``/jobs`` folder. I will call my job ``/jobs/myjob.js``.

```js
exports.run = function() {
    log.info('Hello Job!');
};
```

The job will default run with ``anonymous`` access-rights. So if you need to run as someone else, you will have to do a context-switch 
(see documentation of [context-functions](http://repo.enonic.com/public/com/enonic/xp/docs/6.8.0/docs-6.8.0-libdoc.zip!/module-lib_xp_context.html)).

Next you will have to schedule the job. To schedule jobs, create a file called ``/jobs/jobs.xml`` that contains the following code:

```xml
<jobs>
  <job name="myjob" cron="* * * * *"/>
</jobs>
```

You can have multiple jobs scheduled. The ``name`` is which script to execute and ``cron`` is a cron-pattern 
(see [UINX cron pattern](https://en.wikipedia.org/wiki/Cron)).

