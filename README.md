## Google Hash Code 2018 test solution

This is an example solution for the Google Hash Code 2018 test round challenge written in Java for educational purposes.

The steps I follow with coding challenges:
1) Understand the problem (ask questions until you run out)
2) Find a matching theoretical problem (remove the 'story' from the challenge)
    (e.g. supplier/consumer, Vehicle Routing, Minimum/Maximum search, etc).
3) Google this problem to find theoretical solutions or approaches, learn about weak points, tricks
4) Break down the complexity to multiple, smaller, less complex steps (but keep it flexible in case
    you will change or tweak your approach on the way)
   Think about a simple and brute force solution (it does not have to scale)
5) Draw up an architecture of the solution
6) Implement the mode, the input parsing and output serializing logic
    (e.g. how to load and parse the input file to domain model; how to create a solution file from the domain model with the solution)
7) Come up with test data (if possible)
8) Implement the algorithm
9) Test the brute force solution and submit your result (so you have a result :) )
10) Start optimising


1-2) [the Pizza practice problem](assets/pizza.pdf) is a [Constraint Satisfaction Problem](https://en.wikipedia.org/wiki/Constraint_satisfaction_problem).
    "How to select sub-sets of the set that the sub-sets satisfy certain criteria and the sum of sub-sets covers the largest part of the set possible?"

This test round is a tricky one because the problem is easy to solve using brute force. It is actually hard to resist to the 
dark side :) and think before you type. The catch with the brute force solution is that it will not run in your lifetime since it is O(N!) complex.
Even the small input file (n=6x7=42 is beyond the reach of an O(n!) algorithm. Yes, you can apply memoization but you can see that
tail memoization is quite impossible due to the huge amount of permutations. Head memoization is a bit easier in a form of backtracking,
but even with that, you need lots of tricky optimisation to force the runtime down from O(N!) to a human-lifetime-compatible runtime.
 
So a good advice here, once you have your high level solution, always do a check on its complexity and don't even try to implement something which (as
you will realize 2 hours later) won't even run within time. Try to keep the algorithm as close to O(N) as possible. You got an O(N\*logN)? That's okay!
You have an O(N^2)? Well, you can live with that but this can be in the domain of limiting yourself by waiting for the algorithm to run (remember:
the big input data file has 1000*1000 pizza, which goes up to 10^12 calculations to run. Anything above O(N^2) you shall drop. Don't waste time on that.

3) There are solutions already available on GitHub but I decided to come up with mine before I check those. I can learn more this way.

4) Brute force to the dark side!
```
Iterate through all rows
Iterate through all columns for each row
    If there is a free cell then put a 1x1 slice on top then try to grow it to the right, down,
    left, up by calling the whole function recursively
    Else (if there is no free cell left on the pizza) then calculate its score and if it is 
    bigger then the maximum found so far then
        Update the maximum score and make a copy from the pizza
Once all permutations have been checked (in your grandchild's time), display the slices related 
to the maximum score, easy!
```
I actually spent time with this (I know, ridiculous!) hoping that I'll be able to optimize the code, add memoization, backtraking, whatever
technique helps me out of O(N!) hell. I wish I wouldn't do that. I implemented the algorithm and wasted time with that... then I faced the
inevitable fact that I'll not be able to run it even on the small input file.
4) The 'what if' algorithm
```
Since every slice can only have a certain number of shape, what if we have some kind of filters 
which we can apply to the whole pizza, get the slice scores and if the slice satisfies the 
constraints, add it to a list. Since the filters can be generated outside the runtime, this 
algorithm is actually O(N). 
How to have all the filters? Let's generate them with code :) and only store their 
4 co-ordinate offsets.
```

5) The main objects of the solution:
```
FilterGenerator - to generate the filters

Filters - static class to have access to the generated filters

Filter
    - firstRowOffset
    - firstColumnOffset
    - lastRowOffset
    - lastColumnOffset

FilterAlgorithm - this is the algorithm logic

Model - to parse the input data into
````

6) Issues solved during implementation of the model:
    - there are too many filters to be 'hand written', we need code to generate code    
7) We have example.in, small.in, medium.in and big.in
8) The "oops" effects
    - since the O(N) algorithm is very simple, there were no "oops" effects during implementation.
9) and 10)
    I implemented two algorithm, first with a simple filter matching (FilterMatch) algorithm, then I came up with a variation of this which iterates on the 
    slices to make them bigger (GrowUp).
    
    Here are the statistics for the two algorithms:
    
```    
    | Algorithm   | Input      | Score  | Comment  |
    | ----------- | ---------- | ------ | -------- | 
    | FilterMatch | example.in |     12 |          |
    | GrowUp      | example.in |     15 | +25%     |
    | FilterMatch | small.in   |     35 |          |
    | GrowUp      | small.in   |     40 | +14%     |
    | FilterMatch | medium.in  |  49216 |          |
    | GrowUp      | medium.in  |  48922 | -0.5%    |
    | FilterMatch | big.in     | 894448 |          |
    | GrowUp      | big.in     | 901839 | +0.8%    |
```
    
    You can see that GrowUp is better with big.in but worse with medium.in, but GrowUp gives 
    higher total score overall.
    
    The total score: 951,110