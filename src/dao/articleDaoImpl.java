package dao;

public class articleDaoImpl implements articleDao {
    private DaoFactory daoFactory;

    public articleDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
