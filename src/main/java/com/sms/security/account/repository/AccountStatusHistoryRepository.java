package com.sms.security.account.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sms.security.account.entity.AccountStatusHistory;
public interface AccountStatusHistoryRepository extends JpaRepository<AccountStatusHistory,Long> {}
