package dao;

public class clientDaoImpl implements clientDao {
    private DaoFactory daoFactory;

    public clientDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
