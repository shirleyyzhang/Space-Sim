# Space-Simulator

## Description
Welcome to our project! This is a Space Simulator program made in Java, providing a simple simulation of 
celestial bodies (i.e. stars, planets, moons, and asteroids) in space. It was designed to help high school students 
develop a deeper understanding of the physics that contribute to a solar system by reinforcing their learning with an 
interactive, visual model. 

## Usage
Space Sim operates within the parameters of 20 celestial bodies. The user can use the GUI navigation panels to either 
input different types of celestial bodies and their corresponding properties (e.g. mass, radius, orbit planet/star, 
etc.), or load them from a text file. Using the data read from the file or tracked during the simulation, the 
program can also output various properties of celestial bodies or of the solar system, as specified below.

### Functionalities
* Load and Save celestial body information - Enter a file name or a file number from the list of saved files.
* Display Stats - Displays the properties of all bodies in the simulation.
* Support Life - Enter a planet and check if it can support life
* Shortest Path - Enter two planets and a length of path to check. If the length of path entered is longer than the shortest path between the two planets, the distance will be displayed.
* Older than - Enter a minimum age. The program will return all bodies older than the minimum age entered.
* Add Body - A simple way to add a body during the simulation; requires less specifications than the text file inputs require.
* Largest Moon
* Most Populated Planet
* Oldest Star
* Fastest Asteroid
* List Bodies
* Average Area
* Average Age

### Instructions for Use
1. Run the SpaceRunner.java file.
2. Open the menu, and enter a file name to be added (see `sample.txt` file for a sample input). Press `Add File`
3. Run the simulation by clicking the `Run Sim` button. `Pause Sim` and `Clear Sim` can be used to pause or clear the simulation.
4. To add a body, click `Add Body`, and enter the specifications as prompted. 
Fill the `Orbit Star` field if the body to be entered is a Planet, and the `Orbit Planet` field if the body is a Moon.
After filling the specifications fields, click the corresponding `Add` button depending on the body. Navigate back to the main menu by 
clicking the `Menu` button. 
* To close the menu at any time, press the `Menu` button.
* To display any of the functionalities specified above, enter the necessary fields if required, and click the button of the fields you want displayed. The information should appear on the left panel.
* To save a file, enter a file number to associate with it, and click the `Save File` button. To load that file again, enter the file number and click the `Load File` button.
* At any point, use the `File Names` button. All file names and numbers will be displayed on the left panel.

## Credits
__Collaborators:__
* Samuel Ho
* Susie Choi
* Tony Li
