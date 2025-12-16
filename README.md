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

## :question: Why Not Existing Tools
I considered existing Pomodoro timers, but chose to build my own for the following reasons.
* Steam versions are paid, and I didn't want to spend money for a timer application.
* Smartphone apps often caused me to lose focus while using them, as I would accidentally touch other apps during operation.
* I wanted a tool that works entirely on the PC and doesn't disrupt my workflow.

Additionally, upon reviewing existing tools,<br>
we observed a significant number of users stating they “simply want to use it as a timer,” leading us to conclude that “simplicity holds greater value than advanced features.”

<br>

## :pencil2: Design Policy / Feature Selection
This app intentionally keeps its features to a minimum.<br>
For example, we considered displaying statistics like average work time and average break time, but
* Even with the apps I actually use, I hardly ever look at that information.
* If you have time to look at such information, it would be more reasonable to take a break or move on to the next task.

for this reason, we decided not to implement it.<br>
I prioritize “not adding unused features” and have opted for a design that can be used simply as a timer.

<br>

## UI / UX Design
I are mindful of making it intuitive even for those unfamiliar with PC operations.
* Do not display unnecessary features or information.
* Critical elements (remaining time, control buttons) are displayed prominently.
* A simple screen layout that reduces the number of decisions required for operation.

I designed it with the highest priority on “effortless usability,” minimizing eye movement and operational load.

<img width="386" height="293" alt="Image" src="https://github.com/user-attachments/assets/fd729a9d-187b-4a9f-96a7-e5cafb27b736" />

<br>

## Implemented Features
The following functions are implemented for this timer:
* Pause function during active time
* Option to play a sound when time ends
* Selecting the sound type (intermittent, burst, continuous)
* Choice to continue or end when time expires
* Active time and interval time configurable via direct input

Timer's number can input yourself by keybord.

### Active Time
You can setting number for this range.(Only Integer)<br>
Min 5 ~ Max 120

### Interval Time
You can setting number for this range.(Only Integer)<br>
Min 1 ~ Max 60

<br>

## Timer Behavior
<img width="282" height="132" alt="Image" src="https://github.com/user-attachments/assets/c786c096-21e6-4d6e-aae3-824b8354a351" />

At the end of each time period, the specification requires prompting the user to confirm whether to continue.<br>
This allows for
* Use as a Pomodoro timer
* Use as a simple task timer

It is equipped to handle both of the above.

<br>

## Upcoming Schedule
* Ensure that minimal help information is accessible from within the application.
* Release executable files that run on Mac and Linux, enabling use independent of the operating environment.

Rather than adding more features, we plan to focus on improvements that make it easier to use without hesitation.

