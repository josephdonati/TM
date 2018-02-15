# Sprint 2 - Design
### Design Choices
- Completed Sprint 1 with ideas gathered from Dr. Posnett's solution. I interpreted his advice originally as a need for more methods. After seeing his solution I now understand the benefits/style of decomposing things into more classes. OO style actually "clicked" for me after seeing that solution. My code could be cleaner, but for time/sanity I reworked some of those ideas into the code I already had.  
- Still using try/catch to detect proper user input because it ensures good input before any changes are made to log file.
- Chose to leave in cmdStart, cmdStop, cmdDescribe methods. They are unnecessary and all included code could be placed in the switch cases, but this way keeps switch cleaner, and allows for more clarity inside methods. 
- In TaskLog.readFile() method I changed StrTokenizer to just pick up whatever tokens it finds, and then handle them in the Task class. This simplifies the log read and allows for easier expansion in the future.
- I just changed the String description in the Task class to a StringBuilder, making it simple to append multiple descriptions and preserving the output scheme I used in Sprint 1.

### Limitations / Concerns
- Reused cmdDescribe for new size command since both use same data, have to pass unused variable. I allow this for simplicity and catch it in command, to write different string to log based on available input data
- No input validation for any data input, e.g. if an incorrect task name is provided, it will show a blank summary with the provided task name instead of notifying user "no task found". Allowing because not in spec, and fairly obvious that an error was made.
- When no stop is detected after a start (still running) the time is not accounted for in summary. Not addressed in spec, but I chose to leave out for simplicity. Could notify user in summary that task is "Still Running", and run calculation with time at summary request to account for duration which hasn't been stopped. 

> Written with [StackEdit](https://stackedit.io/).
