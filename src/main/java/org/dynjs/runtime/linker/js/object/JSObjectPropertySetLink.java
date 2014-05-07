package org.dynjs.runtime.linker.js.object;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.js.ReferenceBaseFilter;
import org.dynjs.runtime.linker.js.ReferenceStrictnessFilter;
import org.projectodd.rephract.SmartLink;
import org.projectodd.rephract.builder.LinkBuilder;
import org.projectodd.rephract.guards.Guard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

/**
 * @author Bob McWhirter
 */
public class JSObjectPropertySetLink extends SmartLink implements Guard {

    public JSObjectPropertySetLink(LinkBuilder builder) {
        super(builder);
    }

    public boolean guard(Object receiver, Object context, String propertyName, Object value) {
        return (receiver instanceof Reference) && (((Reference) receiver).getBase() instanceof JSObject);
    }

    @Override
    public MethodHandle guardMethodHandle(MethodType inputType) throws Exception {
        return lookup()
                .findVirtual(JSObjectPropertySetLink.class, "guard", methodType(boolean.class, Object.class, Object.class, String.class, Object.class))
                .bindTo(this);
    }

    public MethodHandle guard() throws Exception {
        return this.builder.getGuard();
    }

    public MethodHandle target() throws Exception {
        return this.builder
                .convert(void.class, Reference.class, ExecutionContext.class, String.class, Object.class)
                .permute(0, 1, 2, 3, 0)
                .filter(0, ReferenceBaseFilter.INSTANCE )
                .filter(4, ReferenceStrictnessFilter.INSTANCE)
                .convert(void.class, JSObject.class, ExecutionContext.class, String.class, Object.class, boolean.class)
                .invoke(lookup().findVirtual(JSObject.class, "get", methodType(Object.class, ExecutionContext.class, String.class)))
                .target();
    }

}
