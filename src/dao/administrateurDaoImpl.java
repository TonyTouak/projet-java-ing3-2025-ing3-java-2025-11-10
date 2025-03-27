package dao;

public class administrateurDaoImpl implements administrateurDao {
    private DaoFactory daoFactory;

    public administrateurDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
