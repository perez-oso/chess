package server;

import dataaccess.MemoryDataAccess;
import service.*;

//public class ServerMain {
//    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
//    }
//}

public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        Server server = new Server(new ChessService(new MemoryDataAccess()));
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}
