package org.dynjs.runtime.linker.java.clazz;

import org.dynjs.runtime.linker.java.DereferencingFilter;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.java.clazz.ConstructLink;
import org.projectodd.rephract.java.instance.UnboundInstanceMethodGetLink;
import org.projectodd.rephract.java.reflect.ResolverManager;

import java.lang.invoke.MethodHandle;

/**
 * @author Bob McWhirter
 */
public class JSJavaConstructLink extends ConstructLink {

    public JSJavaConstructLink(LinkBuilder builder, ResolverManager resolverManager) throws Exception {
        super( builder, resolverManager );
    }

    @Override
    public boolean guard(Object receiver, Object[] args) {
        return super.guard(DereferencingFilter.INSTANCE.filter(receiver), args );
    }

    @Override
    public MethodHandle target() throws Exception {
        this.builder = this.builder.filter( 0, DereferencingFilter.INSTANCE );
        return super.target();
    }
}
