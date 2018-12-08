package com.researchwork;

public interface JavaDatabaseAPI {
    String GetElementByIdTableColumn(String userId, String table, String column);
    String[] getAllUsersFromUsersTable();
    String[] getUserInfoFromTablesByID(String userId);
    void setupDatabase();
    void addToUserTable(String uName, String uSurname, String uLastName, String uDicom, String uResearch);
    void addPicturesOfUserIDIntoResearchTable(String userId, String img1, String img2, String img3, String model3d, String txtfile);
    void RemoveAllInfoOfID(String userId);
}
