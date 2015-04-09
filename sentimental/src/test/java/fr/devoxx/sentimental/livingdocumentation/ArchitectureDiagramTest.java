package fr.devoxx.sentimental.livingdocumentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.livingdocumentation.dotdiagram.DotStyles.ASSOCIATION_EDGE_STYLE;
import static org.livingdocumentation.dotdiagram.DotStyles.IMPLEMENTS_EDGE_STYLE;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.Test;
import org.livingdocumentation.dotdiagram.DotGraph;
import org.livingdocumentation.dotdiagram.DotGraph.Cluster;
import org.livingdocumentation.dotdiagram.DotGraph.Digraph;
import org.livingdocumentation.dotdiagram.DotWriter;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

/**
 * Living Diagram of the Hexagonal Architecture generated out of the code thanks
 * to the package naming conventions.
 */
public class ArchitectureDiagramTest {

	private final DotGraph graph = new DotGraph("Hexagonal Architecture", "LR");

	@Test
	public void generateDiagram() throws Exception {
		final ImmutableSet<ClassInfo> allClasses = ClassPath.from(Thread.currentThread().getContextClassLoader())
				.getTopLevelClasses();

		final Digraph digraph = graph.getDigraph();
		digraph.setOptions("rankdir=LR");

		final String prefix = "fr.devoxx.sentimental.";
		Stream<ClassInfo> domain = allClasses.stream().filter(filter(prefix, "domain"));
		final Cluster core = digraph.addCluster("hexagon");
		core.setLabel("Core Domain");

		// add all domain model elements first
		domain.forEach(new Consumer<ClassInfo>() {
			public void accept(ClassInfo ci) {
				final Class clazz = ci.load();
				core.addNode(clazz.getName()).setLabel(clazz.getSimpleName()).setComment(clazz.getSimpleName());
			}
		});

		Stream<ClassInfo> infra = allClasses.stream().filter(filter(prefix, "infra"));
		infra.forEach(new Consumer<ClassInfo>() {
			public void accept(ClassInfo ci) {
				final Class clazz = ci.load();
				digraph.addNode(clazz.getName()).setLabel(clazz.getSimpleName()).setComment(clazz.getSimpleName());
			}
		});
		infra = allClasses.stream().filter(filter(prefix, "infra"));
		infra.forEach(new Consumer<ClassInfo>() {
			public void accept(ClassInfo ci) {
				final Class clazz = ci.load();
				// API
				for (Field field : clazz.getDeclaredFields()) {
					final Class<?> type = field.getType();
					if (!type.isPrimitive()) {
						digraph.addExistingAssociation(clazz.getName(), type.getName(), null, null,
								ASSOCIATION_EDGE_STYLE);
					}
				}

				// SPI
				for (Class intf : clazz.getInterfaces()) {
					digraph.addExistingAssociation(intf.getName(), clazz.getName(), null, null, IMPLEMENTS_EDGE_STYLE);
				}
			}
		});

		// then wire them together
		domain = allClasses.stream().filter(filter(prefix, "domain"));
		domain.forEach(new Consumer<ClassInfo>() {
			public void accept(ClassInfo ci) {
				final Class clazz = ci.load();
				for (Field field : clazz.getDeclaredFields()) {
					final Class<?> type = field.getType();
					if (!type.isPrimitive()) {
						digraph.addExistingAssociation(clazz.getName(), type.getName(), null, null,
								ASSOCIATION_EDGE_STYLE);
					}
				}

				for (Class intf : clazz.getInterfaces()) {
					digraph.addExistingAssociation(intf.getName(), clazz.getName(), null, null, IMPLEMENTS_EDGE_STYLE);
				}
			}
		});

		// render into image
		final Properties p = new Properties();
		final String fileName = "graphviz-dot.properties";
		final InputStream is = this.getClass().getResourceAsStream(fileName);
		if (is == null) {
			throw new RuntimeException("Could not load property file '" + fileName);
		}
		p.load(is);
		final DotWriter dotWriter = new DotWriter(p);
		final String content = graph.render().trim();
		final String imageName = dotWriter.toImage("hexagonal-architecture", content);

		assertThat(imageName).isEqualTo("hexagonal-architecture.png");
	}

	private Predicate<ClassInfo> filter(final String prefix, final String layer) {
		return new Predicate<ClassInfo>() {
			public boolean test(ClassInfo ci) {
				final boolean nameConvention = ci.getPackageName().startsWith(prefix)
						&& !ci.getSimpleName().endsWith("Test") && ci.getPackageName().contains("." + layer);
				return nameConvention;
			}

		};
	}
}
