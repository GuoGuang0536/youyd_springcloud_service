package com.youyd.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyd.article.dao.PlatformDao;
import com.youyd.article.pojo.Article;
import com.youyd.cache.constant.RedisConstant;
import com.youyd.cache.redis.RedisService;
import com.youyd.pojo.QueryVO;
import com.youyd.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 前台文章
 * @author: LGG
 * @create: 2018-10-13 16:39
 **/
@Service
public class PlatformService {

	@Autowired
	private PlatformDao platformDao;

	@Autowired
	private RedisService redisService;

	/**
	 * 查询全部列表
	 * @return
	 */
	public IPage<Article> findArticleByCondition(Article article, QueryVO queryVO){
		Page<Article> page = new Page<Article>(queryVO.getPage(),queryVO.getLimit());
		IPage<Article> articlePage = platformDao.findArticlePage(page,queryVO);
		return articlePage;
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Article findArticleByPrimaryKey(String id) {
		Article article = null;
		try {
			Object mapJson = redisService.get(RedisConstant.REDIS_KEY_ARTICLE + id);
			if (mapJson != null) {
				return JsonUtil.mapToPojo(mapJson, Article.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		article = platformDao.selectById(id);
		System.out.println("555");
		try {
			redisService.set(RedisConstant.REDIS_KEY_ARTICLE + id, article, RedisConstant.REDIS_TIME_DAY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return article;

	}

	/**
	 * 增加
	 * @param article
	 */
	public void insertArticle(Article article) {
		platformDao.insert(article);
	}

	/**
	 * 修改
	 * @param article
	 */
	public void updateByPrimaryKeySelective(Article article) {
		redisService.del( "article_" + article.getId());
		platformDao.updateById(article);
	}

	/**
	 * 删除
	 * @param articleIds:文章id集合
	 */
	public void deleteByIds(List articleIds) {
		redisService.del( "article_" + articleIds );
		platformDao.deleteBatchIds(articleIds);
	}


}
