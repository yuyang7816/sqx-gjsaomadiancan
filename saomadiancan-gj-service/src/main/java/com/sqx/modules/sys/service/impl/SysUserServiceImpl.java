package com.sqx.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.modules.datacentre.dao.ShopAdminDao;
import com.sqx.modules.datacentre.entity.SysUserShop;
import com.sqx.modules.order.entity.TbOrder;
import com.sqx.modules.sys.dao.SysRoleDao;
import com.sqx.modules.sys.dao.SysUserDao;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.sys.entity.SysUserRoleEntity;
import com.sqx.modules.sys.service.SysRoleService;
import com.sqx.modules.sys.service.SysUserRoleService;
import com.sqx.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 */
@SuppressWarnings("ALL")
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private ShopAdminDao shopAdminDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Integer page = Integer.parseInt(params.get("page").toString());
		Integer limit = Integer.parseInt(params.get("limit").toString());
		String username = (String)params.get("username");
		Long createUserId = (Long)params.get("createUserId");

		/*IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.eq(createUserId != null,"create_user_id", createUserId)
		);*/
		Page<SysUserEntity> pages=new Page<>(page,limit);
		IPage<SysUserEntity> pageList = sysUserDao.selectSysUserList(pages, username);
		for(int i = 0;i<pageList.getRecords().size();i++){
			List<SysUserRoleEntity> sysUserRoleEntities = sysRoleDao.selectRoleList(pageList.getRecords().get(i).getUserId());
			pageList.getRecords().get(i).setRoleList(sysUserRoleEntities);
			List<SysUserShop> sysUserShops = shopAdminDao.selectShopList(pageList.getRecords().get(i).getUserId());
			pageList.getRecords().get(i).setShopList(sysUserShops);
		}
		return new PageUtils(pageList);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@SuppressWarnings("AlibabaTransactionMustHaveRollback")
	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		//添加管理员账号
		//this.save(user);
		sysUserDao.insertSysUser(user);
		Long userId = user.getUserId();//返回的主键user_id
		//如果该账号角色为商铺管理员，则添加管理员—商铺关联表
		if(user.getRoleIdList().contains(12L)){
			for(int i = 0;i<user.getShopIdList().size();i++){
				SysUserShop sysUserShop = new SysUserShop();
				sysUserShop.setShopId(user.getShopIdList().get(i));
				sysUserShop.setUserId(userId);
				shopAdminDao.insert(sysUserShop);
			}
		}

		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);

		//如果该账号角色为商铺管理员，则先删除所有商铺，再添加管理员—商铺关联表
		if(user.getRoleIdList().contains(12L)){
			//删除
			shopAdminDao.delete(new QueryWrapper<SysUserShop>().eq("user_id", user.getUserId()));
			//添加
			for(int i = 0;i<user.getShopIdList().size();i++){
				SysUserShop sysUserShop = new SysUserShop();
				sysUserShop.setShopId(user.getShopIdList().get(i));
				sysUserShop.setUserId(user.getUserId());
				shopAdminDao.insert(sysUserShop);
			}
		}

		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

    @Override
    public List<SysUserShop> getShopList(Long userId) {

		return baseMapper.getShopList(userId);


    }

    /**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		/*if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new SqxException("新增用户所选角色，不是本人创建");
		}*/
	}
}