package dao;

public class commandeDaoImpl implements commandeDao {
    private DaoFactory daoFactory;

    public commandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
