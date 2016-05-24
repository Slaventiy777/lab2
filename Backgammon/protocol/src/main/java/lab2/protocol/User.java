package lab2.protocol;

public class User {

    private String nickname;
    private String password;
    private int rank;
    private String opponent;
    private long roomID;

    public enum Status {Plays, Expects, Disconnected};

    Status userStatus;

    public User(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
        this.rank = 1000;
    }

    public User(String nickname, String password, int rank) {
        this.nickname = nickname;
        this.password = password;
        this.rank = rank;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getRank() {
        return rank;
    }

    public Status getUserStatus() {
        return userStatus;
    }

    public String getOpponent() {
        return opponent;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }


    public boolean equalsAuthorization(User user) {

        if(user == null)
            return false;

        if(getClass() != user.getClass())
            return false;

        if(user == this)
            return true;

        return nickname.equals(user.nickname)&& password.equals(user.password);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(obj == null)
            return false;

        if(getClass() != obj.getClass())
            return false;

        User tempUser = (User) obj;

        return nickname.equals(tempUser.nickname) && password.equals(tempUser.password) && rank == tempUser.rank
                && userStatus.equals(tempUser.userStatus) && opponent.equals(tempUser.opponent) && roomID == tempUser.roomID;
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public String toString() {
        return "Nickname: [" + this.nickname + "] password: [" + this.password +"] rank [" + this.getRank() + "] satete ["
                + this.getUserStatus() + "] playWith [" + this.getOpponent() + "] roomID [" + this.getRoomID() + "] ";
    }

}

