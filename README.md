# Pomodoro Timer
## Overview
This application is a simple Pomodoro timer for PC users designed to prevent problems arising from excessive concentration that causes you to lose track of time,
and to help you take appropriate breaks and create natural breaks in your work.

It is designed with the goal of learning Java, while also being intended for daily, continuous use.

<br>

## Motivation
When I became too absorbed in my work, my time management would break down, and before I knew it, I'd be working for long stretches without taking breaks. To counter this, I decided a “mechanism to force breaks” was necessary. I also wanted to practice Java, so I decided to build my own Pomodoro timer.
My goal is to create a tool I can actually use consistently, not just a simple learning sample.

<br>

## Why Not Existing Tools
I considered existing Pomodoro timers, but chose to build my own for the following reasons.
* Steam versions are paid, and I didn't want to spend money for a timer application.
* Smartphone apps often caused me to lose focus while using them, as I would accidentally touch other apps during operation.
* I wanted a tool that works entirely on the PC and doesn't disrupt my workflow.

Additionally, upon reviewing existing tools,<br>
we observed a significant number of users stating they “simply want to use it as a timer,” leading us to conclude that “simplicity holds greater value than advanced features.”

<br>

## Design Policy / Feature Selection
This app intentionally keeps its features to a minimum.<br>
For example, we considered displaying statistics like average work time and average break time, but
* Even with the apps I actually use, I hardly ever look at that information.
* If you have time to look at such information, it would be more reasonable to take a break or move on to the next task.

for this reason, we decided not to implement it.<br>
We prioritize “not adding unused features” and have opted for a design that can be used simply as a timer.
<br>

## UI / UX Design
We are mindful of making it intuitive even for those unfamiliar with PC operations.
* Do not display unnecessary features or information.
* Critical elements (remaining time, control buttons) are displayed prominently.
* A simple screen layout that reduces the number of decisions required for operation.

We designed it with the highest priority on “effortless usability,” minimizing eye movement and operational load.
<br>

## Upcoming Schedule
* Ensure that minimal help information is accessible from within the application.
* Release executable files that run on Mac and Linux, enabling use independent of the operating environment.

Rather than adding more features, we plan to focus on improvements that make it easier to use without hesitation.

