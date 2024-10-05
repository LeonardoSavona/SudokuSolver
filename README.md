# Sudoku Solver
This project was created with the goal of solving any 9x9 Sudoku regardless of difficulty.

## Strategies

Various strategies are adopted to solve a sudoku, below list of those used by the program.
The more the difficulty increases the more strategies we should use.

#### Basic Strategy

The basic strategy consists in identifying, for each cell, its possible numbers, based on the numbers already present in the same row, in the same column and in the same "square". 
If there is only one possible number, then it will be the value of the cell.

![img_1.png](images/img_1.png)

### Possible Values

As the difficulty increases it will no longer be possible to find the value of a cell immediately, so we should write down the possible values for each cell and work with them.

![img.png](images/img.png)

Obviously the possible values will have to be updated every time we find the value of a cell.

In the example we found the number 1 (in the cell circled in red) and therefore we will remove the number 1 from the possible cell values in the same row, same column and same "square".
![img_2.png](images/img_2.png)

#### Basic Possible Values Strategy

The simplest strategy to adopt using the possible cell values is to check whether a number is repeated only once in a row, column or square.

In this example, among the possible values of the highlighted column, the number 4 is present only in one cell, therefore the value of that cell will necessarily be 4.

![img_3.png](images/img_3.png)

#### Couple of Candidates Strategy

This strategy consists of analyzing each row, column and square to identify 2 cells, both with exactly 2 possible values that coincide.

If identified, it means that the 2 possible values necessarily belong to those 2 cells, so we can remove them from the possible values of the other cells in row, column or square.

![img_4.png](images/img_4.png)

In this example we found two cells in column 4 with 2 values that coincide (1 and 6), so we can remove the possible values 1 and 6 from the other cells in this column.

![img_5.png](images/img_5.png)

#### Trio of Candidates Strategy
