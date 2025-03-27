package dao;

public class utilisateurDaoImpl implements utilisateurDao {
    private DaoFactory daoFactory;

    public utilisateurDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
