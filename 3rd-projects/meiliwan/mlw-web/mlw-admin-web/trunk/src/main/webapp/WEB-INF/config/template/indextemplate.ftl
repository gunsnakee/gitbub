<div class="s1">
    <h2><a href="/allcategory.html" class="n_all">所有商品分类</a></h2>
    <div class="snavP">
        <ul class="s_nav">
            <#list pc as entity>
                <li>
                    <#--<h3><a href="/category/1/${entity.id}.html">${entity.name}</a></h3>-->
                    <h3><a href="/product/category-${entity.id}-0-0-0-0-0-0-0-0-0-1-1-1">${entity.name}</a></h3>
                    <#if entity.children?exists>
                        <div class="s_tip s_t01">
                             <#list entity.children as entity1>
                                <dl>
                                    <#--<dt><a href="/category/2/${entity1.id}.html">${entity1.name}</a></dt>-->
                                    <dt><a href="/product/category-${entity1.pId}-${entity1.id}-0-0-0-0-0-0-0-0-1-1-1">${entity1.name}</a></dt>
                                    <#if entity1.children?exists>
                                        <dd>
                                            <#list entity1.children as entity2>
                                                <a href="/product/category-${entity1.pId}-${entity1.id}-${entity2.id}-0-0-0-0-0-0-0-1-1-1">${entity2.name}</a>  <#if entity2_has_next>|</#if>
                                            </#list>
                                        </dd>
                                    </#if>
                                </dl>
                            </#list>
                        </div>
                    </#if>
                </li>
            </#list>
        </ul>
    </div>
</div>