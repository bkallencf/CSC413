classDiagram
Game *-- Board
Game *-- Move
Game *-- Color
Game ..> MoveGenerator
Game : Board board
Game : List<Move> history
Game : Color sideToMove
Game : Game()
Game : Game(Board, Color)
Game : Board board()
Game : Color sideToMove()
Game : List<Move> history()
Game : List<Move> legalMoves()
Game : Optional<Move> findlegalMoves(String)
Game : play(Move)
Game : Optional<Move> undoLastMove()
MoveGenerator ..> Board
MoveGenerator ..> Color
MoveGenerator ..> Position
MoveGenerator ..> Piece
MoveGenerator ..> Move
MoveGenerator : List<Move> legalMoves(Board, Color)
MoveGenerator : boolean isAttacked(Board, Position, Color)
MoveGenerator : boolean isInCheck(Board, Color)
Piece <|-- Pawn
Piece <|-- Knight
Piece <|-- Bishop
Piece <|-- Rook
Piece <|-- Queen
Piece <|-- King
Piece *-- Color
Piece *-- PieceType
Piece ..> Move
Piece ..> Board
Piece ..> Position
Piece : Color color
Piece : PieceType pieceType
Piece : Color color()
Piece : PieceType type()
Piece : char symbol()
Piece : List<Move> pseudoLegalMoves(Board, Position)
Piece : boolean attacks(Board, Position, Position)
Piece : String toString()
Board *-- Piece
Board ..> Position
Board ..> Color
Board ..> Move
Board : Piece[][] pieces
Board : Board board()
Board : Piece pieceAt(Position)
Board : List<Position> positionsOf(Color)
Board : Position kingPosition(Color)
Board : place(Position, Piece)
Board : boolean isEmpty(Position)
Board : apply(Move)
Board : undo(Move)
Board : String toString()
Move *-- Position
Move *-- PieceType
Move : Position from
Move : Position to
Move : Piece moved
Move : Piece captured
Move : PieceType promotesTo
Move : Move quiet(Position, Position, Piece)
Move : Move capture(Position, Position, Piece, Piece)
Move : Move promotion(Position, Position, Piece, Piece, PieceType)
Move : boolean isCapture()
Move : boolean isPromotion()
Move : String toString()
Position : Position()
Position : Position parse(String)
Position : Position offsetOrNull(int, int)
Position : String toString()
PieceType : char symbol()
PieceType : PieceType fromSymbol(char)
Color : Color opposite()
Color : int pawnDirection()
Color : int pawnStartRank()
Color : int promotionRank()