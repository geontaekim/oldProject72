package kr.co.seoulit.logistics.sys.interceptor;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;

public class SiteMeshFilter extends ConfigurableSiteMeshFilter{
	
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "/WEB-INF/views/decorator/decorator.jsp")
			.addExcludedPath("*.do")
			.addExcludedPath("*login*")
			.addExcludedPath("*logout*");
		builder.addTagRuleBundle(new DivExtractingTagRuleBundle());
	}
}
