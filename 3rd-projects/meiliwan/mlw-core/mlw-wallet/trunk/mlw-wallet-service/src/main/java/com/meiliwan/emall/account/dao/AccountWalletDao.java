package com.meiliwan.emall.account.dao;

import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.core.db.IDao;

public interface AccountWalletDao extends IDao<Integer, AccountWallet>{

  int freezeMoney(int uid,double freezeMoney);
  int unFreezeMoney(int uid,double freezeMoney);
}