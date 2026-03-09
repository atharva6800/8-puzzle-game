# 🧩 8 Puzzle Game with AI Solver

## Overview

This project implements the classic **8-Puzzle Game** with an integrated **AI solver**. The goal of the game is to arrange the numbered tiles in order from **1 to 8** with the blank tile at the end.

The project contains two implementations:

1. **Web Version** – Built using HTML, CSS, and JavaScript with smooth tile animations.
2. **Java Desktop Version** – Built using Java Swing with advanced features such as scoring, timer, hints, and auto-solve using the A* algorithm.

---

# Features

## Core Game Features

* Interactive 3×3 puzzle board
* Tile movement with blank space
* Randomized solvable puzzle generation
* Move counter

## AI Features

* Automatic puzzle solving
* Optimal path calculation
* Heuristic-based search

## Web Version Features

* Smooth tile animations
* Hint system
* Auto-solve animation
* Initial vs goal state visualization
* Player vs AI move comparison

## Java Desktop Version Features

* Graphical user interface using Swing
* Difficulty levels (Easy, Medium, Hard)
* Timer and scoring system
* Hint highlighting
* Auto-solve using A* algorithm
* High score saving
* Sound feedback

---

# Technologies Used

## Web Application

* HTML
* CSS
* JavaScript

## Desktop Application

* Java
* Java Swing

## Algorithms

* Breadth First Search (BFS) – Web solver
* A* Search Algorithm – Java solver
* Manhattan Distance Heuristic

---

# Project Structure

```
8-puzzle-project/
│
├── index.html              # Web implementation
├── EightPuzzleGUI.java     # Java desktop application
├── EightPuzzleGUI.class
├── EightPuzzleGUI$Node.class
└── highscore.txt           # Generated automatically for desktop version
```

---

# How to Run the Project

## Run the Web Version

1. Download or clone the repository
2. Open the file:

```
index.html
```

3. The game will run directly in your web browser.

No installation required.

---

## Run the Java Desktop Version

### Step 1: Compile

```
javac EightPuzzleGUI.java
```

### Step 2: Run

```
java EightPuzzleGUI
```

Make sure **Java JDK 8 or higher** is installed.

---

# How to Play

1. Click on a tile next to the blank space to move it.
2. Arrange the numbers in order:

```
1 2 3
4 5 6
7 8 _
```

3. Use the following options:

* **Restart** – Generate a new puzzle
* **Hint** – Shows the best next move
* **Auto Solve** – AI solves the puzzle

---

# AI Solver Explanation

### Web Version

Uses **Breadth First Search (BFS)** to guarantee the shortest solution path.

### Java Version

Uses the **A* Search Algorithm** with **Manhattan Distance heuristic**:

```
f(n) = g(n) + h(n)
```

Where:

* `g(n)` = cost from start
* `h(n)` = estimated distance to goal

This significantly reduces the search space.

---

# Future Improvements

* Add leaderboard system
* Mobile responsive UI
* Multiplayer puzzle race
* Image-based sliding puzzle
* Larger puzzles (15 Puzzle)

---

# Author

Project developed as an implementation of **Artificial Intelligence search algorithms** applied to the classic sliding puzzle game.

---

# License

This project is open-source and free to use for educational purposes.
