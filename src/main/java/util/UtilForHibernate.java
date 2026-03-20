package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class UtilForHibernate {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    // using private cause we will create it just once, such a heavy operation about Session Factory creation
    private static SessionFactory buildSessionFactory(){

        try{
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable exception) {
            System.out.println("Creation of Session factory is failed!" + exception.getMessage());
            throw new ExceptionInInitializerError(exception);
        }

    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void closeSessionFactory(){getSessionFactory().close();}




}
