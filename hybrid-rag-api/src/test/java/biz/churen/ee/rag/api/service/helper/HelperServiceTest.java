package biz.churen.ee.rag.api.service.helper;

import org.junit.Test;

import biz.churen.ee.rag.api.HybridRagApiApplicationTest;
import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;

public class HelperServiceTest extends HybridRagApiApplicationTest {
    @Resource
    private HelperService helperService;

    @Test
    public void confidenceScore() {
        String ada_lovelace_article = """
                会见开始前的合影环节，科斯塔主席、冯德莱恩主席主动向习近平主席伸出右手，三位领导人微笑着携手面向记者合影留念。
                  今年是中欧建交50周年。习近平主席同欧方领导人围绕中欧关系发展，回顾历史，立足当前，展望未来，深入交流。
                  “中欧都是国际社会中的‘大个子’”——会见中，习近平主席生动而形象地描述世界第二和第三大经济体的重要地位。
                  大就要有大的样子，大就要有大的担当。当前，百年变局加速演进，国际形势变乱交织，单边主义、保护主义、霸凌行径严重冲击国际秩序和国际规则。中欧携手合作的时代意义更加凸显。
                  “中欧都是主张多边主义、倡导开放合作的建设性力量，国际形势越是严峻复杂，中欧就越要加强沟通、增进互信、深化合作。”习近平主席深刻指明中欧关系的重要性。""";


        Pair<Boolean, Float> v1 = helperService.confidenceScore("中国主席出席了会议",
                ada_lovelace_article);
        System.out.println("======> " + JSONUtil.toJsonStr(v1));
        Pair<Boolean, Float> v2 = helperService.confidenceScore("和经济相关吗?",
                ada_lovelace_article);
        System.out.println("======> " + JSONUtil.toJsonStr(v2));
    }
}
