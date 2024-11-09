
# Development Guide

## Commit naming:
For commit naming the following convention will be used

`<type>[optional scope]: <description>`

If there is a major change, add a `!` before the type.

###### Types
- fix: for patching a bug in the codebase.
- feat: for introducing a new feature to the codebase.
- refactor: for rewriting code structure without changing behaviour.
- docs: for adding only changes related to documentation.

###### Scopes
Scopes are useful to provide contextual information of which area is the commit related to:
- [UI]: for changes related to the graphical interface.
- [Agent]: for changes related to an agent's behaviour.
- [Net]: for changes related to the network communication.
- [Core]: for changes related to the overall behaviour of the program.
- [Concurrency]: for changes related to concurrency management, such as critical sections, semaphores or thread handling.
- [Sound]: for changes related to audio features, including sound effects and audio triggers based on events.

##### Examples

- feat[Agent]: implement vendor and ticket seller agents with unique behaviors
- feat[Sound]: add background stadium crowd noise and event sounds
- !fix[Agent]: resolve issue with fans not leaving after the game ends
- refactor[Concurrency]: optimize buffer handling for food and ticket booths

## Branch Naming

`<type>/<scope>/<short-description>`

###### Branch Types
- feature: For new features or major functionality.
- bugfix: For bug fixes.
- hotfix: For urgent fixes that need to be applied to the main branch immediately.
- refactor: For refactoring efforts that don’t add new features but improve code structure or performance.
- docs: For documentation updates or additions.
- test: For testing features.

###### Examples
- feature/vendor_agent
- bugfix/button_overlap
- hotfix/agent_termination
- docs/update_readme