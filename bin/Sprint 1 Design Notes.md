
# Sprint 1 - Design
### Design Choices
- I tried to decompose the program with methods to handle almost every action instead of coding more things together. This lead to more method calls, and was inspired by Dr. Posnett's advice in class. I would typically repeat more coding, with less decomposed nature. I can't honestly say I had a good reason for doing this other than to follow the professor's advice, but after doing so, can clearly see the benefit. 
- Could have combined cmdStart and cmdStop methods, but left separate for clarity, and possibility of needing to modify later.
- Used FileWriter with a PrintWriter to write individual lines to log file, Scanner to read back.
- To handle the reading of the log file for the summary I came up with a solution which reads each line of text from the log into a StringTokenizer. Each token is then then added to an ArrayList so that the tokens can be accessed individually for comparison or subtraction (calculating duration). Each ArrayList would then represent a single line of the log, and each ArrayList would be stored in a Linked list. This would allow comparison between elements, between lines. With the LinkedList of ArrayLists, I was envisioning a sort of 2D array which would allow searching and access for individual tokens in the log file.
- I separated the tokens in each line in the log with a slash, "/", so I could use the StringTokenizer to separate each portion of the line, while keeping the description Strings intact
### Limitations / Concerns
- I ran out of time before being able to finish this assignment. This was caused by two things, underestimating the complexity and time requirements of the project, and waiting until it was too late to start working. Life has been hectic (wife and I have the flu, newborn baby, etc., etc.,), but I will work to not let this happen again, namely by starting assignments as soon as they are assigned.
- Summary methods were unfinished and untested. 
- I had not yet devised a solution to calculate duration from my LocalDateTime time stamps in the log, not sure the LocalDateTime would have ended up working, or would have had to calculate duration manually, parsing the time codes to ints and doing the math myself
- I had not yet devised a way to combine individual task summaries to overall summary.
- Can only accept specific input, e.g. "start" not "Start". I'm accepting this because the users are programmers, and command line input is typically case-sensitive. 
- I have a lot of IOExceptions suggested by Eclipse, necessary to make the project run correctly, which I don't fully understand. I get the concept of the required exceptions, with each calling method requiring an exception "up the chain". They seem excessive, though, and I feel like there is a more elegant solution which I am not seeing. 

> Written with [StackEdit](https://stackedit.io/).
