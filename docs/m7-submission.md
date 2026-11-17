1. Group: Benji Kallen

2. Tools: All code was written by hand with help from Oracle and Stack Overflow

3. pgn

4. N/A

5. N/A

6. N/A

7. N/A

8. The currently selected square is held inside BoardPanel as it knows all the PiecePanels and is responsible for all of their states. In PGNView, there isn't really such a thing as a selected square. The for loop inside of nextAction briefly converts the given notation from standard algebraic format (albeit without castling, check, and checkmate notation), and converts into a usable string for PlayerAction to parse.

9. They simply rely on Game to define the legal moves. If the player's request is inside those legal moves, it works.

10. On promotion, the SwingView opens up a JDialogue window and freezes action on the main window until the user has selected a piece to promote to. In the PGN view, the user must specify a piece to promote to via "=X" notation.

11. I have practice with Swing since I used it 2 years ago to create an AI math tutor, and having since learned a lot more JavaScript, I felt much more equipped to create a Swing view. When designing the Swing view, I was mostly working with the design philosophy of being able to change the colors and board size and piece representations at will without having to change that much. As such, changing those things isn't very difficult. Meanwhile, PGNView is a pretty standard view that doesn't have a lot to change about it. Even simply increasing the size would be semi-difficult.

12. No code from AI or teammates. Reading from Stack Overflow and Oracle has been credited as such throughout the code.

13. My code had 2 major bugs in how the pawn's right attack was functioning and how the first move in each piece's sliding moves. It wasn't too hard to narrow down. As such, I had a pretty good idea of where to look and find it.

14. All code was written by hand. A great deal of Stack Overflow and Oracle documentation was utilized in the making of the Swing view, as well as consultation with my aforementioned previous Swing project.