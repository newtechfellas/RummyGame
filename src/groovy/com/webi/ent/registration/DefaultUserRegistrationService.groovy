package com.webi.ent.registration

import com.webi.ent.common.vo.RegistrationVO
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Repository
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

import javax.sql.DataSource

@Repository
public class DefaultUserRegistrationService implements IUserRegistrationService, ApplicationContextAware {
    ApplicationContext applicationContext;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    DataSource datasource;

    @Transactional
    @Override
    public void resisterUser(final RegistrationVO registrationVO)
            throws DuplicateUserRegistrationException {
        //Check if the UserId already exists
        if (isUserRegistered(registrationVO)) {
            throw new DuplicateUserRegistrationException()
        }

        // construct an appropriate transaction manager
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(datasource);
        TransactionTemplate tx = new TransactionTemplate(txManager);
        tx.execute(new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(TransactionStatus status) {
                UserRegistryEntity userRegistryEntity = new UserRegistryEntity().with {
                    userId = registrationVO.emailId
                    password = registrationVO.password
                    userName = registrationVO.userName
                    registerDate = new java.util.Date()
                }
                sessionFactory.getCurrentSession().save(userRegistryEntity);
            }
        });
    }

    /**
     * @param registrationVO
     * @return
     */
    public boolean isUserRegistered(RegistrationVO registrationVO) {
        boolean isRegistered = false;
        UserDetailsService userDetailsService = applicationContext.getBean("userDetailsService", UserDetailsService.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(registrationVO.getEmailId());
        if (userDetails != null) {
            isRegistered = true;
        }
        return isRegistered;
    }

    @Override
    public void unregisterUser(RegistrationVO registrationVO) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<RegistrationVO> showRegisterUsers() {
        // TODO Auto-generated method stub
        return null;
    }

}
