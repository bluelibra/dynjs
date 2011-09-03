package org.dynjs.compiler;


import me.qmx.jitescript.JDKVersion;
import me.qmx.jitescript.JiteClass;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynFunction;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.DynamicClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DynJSCompiler {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String PACKAGE = "org.dynjs.gen.".replace('.', '/');
    private static final boolean DEBUG = false;

    public Function compile(final DynFunction arg) {
        String className = PACKAGE + "AnonymousDynFunction" + counter.incrementAndGet();
        JiteClass jiteClass = new JiteClass(className, new String[]{p(Function.class)}) {{
            defineDefaultConstructor();
            defineMethod("call", ACC_PUBLIC, sig(DynAtom.class, DynThreadContext.class, Scope.class, DynAtom[].class), arg.getCodeBlock());
        }};
        final DynamicClassLoader classLoader = new DynamicClassLoader();
        byte[] bytecode = jiteClass.toBytes(JDKVersion.V1_7);
        if (DEBUG) {
            ClassReader reader = new ClassReader(bytecode);
            reader.accept(new TraceClassVisitor(new PrintWriter(System.out)), ClassReader.EXPAND_FRAMES);
        }
        Class<?> functionClass = classLoader.define(className.replace('/', '.'), bytecode);
        try {
            return (Function) functionClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}