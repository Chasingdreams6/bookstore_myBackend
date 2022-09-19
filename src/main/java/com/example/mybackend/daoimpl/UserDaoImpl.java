package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository     // 数据库访问类
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    /*
     * add a user
     * @param user
     * @return lineNumber that effected, >= 1 is success
     * */
    public int insert(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        int res = (int) session.save(user);
        session.getTransaction().commit();
        session.close();
        return res;
    }

    // delete a user
    public Integer deleteByID(Long userid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        int res = session.createQuery("delete from User where id=:userid")
                .setParameter("userid",userid).executeUpdate();
        session.getTransaction().commit();
        return res;
    }

    // update a user
    public Integer update(User user) {
        //User oldUser = SearchByID(user.getId());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
//        oldUser.setUsername(user.getUsername());
//        oldUser.setUserpassword(user.getUserpassword());
//        oldUser.setUsergender(user.getUsergender());
//        oldUser.setUseraddress(user.getUseraddress());
//        oldUser.setUsermail(user.getUsermail());
//        oldUser.setUserphone(user.getUserphone());
//        session.update(oldUser);
        session.merge(user);
        session.getTransaction().commit();
        return Constants.SUCCESS;
    }

    public User SearchByName(String name) {
        logger.info("startSearch");
        return userRepository.findUserByUsername(name);
    }

    public User SearchByID(Integer id) {
        return userRepository.findUserById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User mostCostUser() {
        List<User> users = getUsers();
        if (users == null) return null;
        int maxCost = 0, id = 0;
        for (int i = 0, size = users.size(); i < size; ++i) {
            User user = users.get(i);
            int curCost = 0;
            for (Order order : user.getOrders()) {
                for (OrderItem item : order.getOrderItems()) {
                    curCost += item.getBooknumber() * item.getCurprice();
                }
            }
            //System.out.println(curCost);
            if (maxCost > curCost) {
                id = i;
                maxCost = curCost;
            }
        }
        return users.get(id);
    }

    public List<MostCostUser> usersSortedByCost(String start, String end) {
        List<User> users = getUsers();
        List<MostCostUser> res = new ArrayList<>();
        for (User user : users) {
            if (user.getValid() == 0 || user.getValid() == 1)
                res.add(new MostCostUser(user.getUsername(), user.getCostByTime(start, end)));
        }
        res.sort(new Comparator<MostCostUser>() {
            public int compare(MostCostUser o1, MostCostUser o2) {
                return o2.getCost().compareTo(o1.getCost());
            }
        });
        for (MostCostUser user : res) {
            System.out.println(user.getName() + " " + user.getCost());
        }
        return res;
    }
}
