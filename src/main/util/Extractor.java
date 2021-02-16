/**
 * @description: extractor fundamental information of project structure base on a single file
 * @author: fancying
 * @create: 2019-05-25 12:02
 **/
package main.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import main.GraphInfo.ClassInfo;
import main.GraphInfo.MethodInfo;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Extractor {

    private HashSet<ClassInfo> classInfos;
    private String projectName;
    private String packageName;
    private String fileName;

    public Extractor(String path, String projectName) {
        this.projectName = projectName;
//        classInfos = new HashSet<>();
        try {
            CompilationUnit compilationUnit = JavaParser.parse(new File(path));
            parsePackageName(compilationUnit);
            String[] singleDir = path.replace('\\','/').split("/");
            fileName = singleDir[singleDir.length - 1];
            parseClassInterface(compilationUnit);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parsePackageName(CompilationUnit cu) {
        if (cu.getPackageDeclaration().isPresent()) {
            packageName = cu.getPackageDeclaration().get().getName().asString();
        }
    }

    private void parseClassInterface(CompilationUnit cu) {

        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarationList = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarationList) {
            //ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration = classOrInterfaceDeclaration.resolve();
            ClassInfo classInfo = new ClassInfo();
            List<String> extendNames = new ArrayList<>();
            List<String> implementedNames = new ArrayList<>();

            // 名字
            String classOrInterfaceName = classOrInterfaceDeclaration.getName().asString();
            // 修饰符
            StringBuilder sb = new StringBuilder();
            for (Modifier modifier : classOrInterfaceDeclaration.getModifiers()) {
                sb.append(modifier.asString());
                sb.append(" ");
            }
            // 扩展的类名
            for (ClassOrInterfaceType extendedType : classOrInterfaceDeclaration.getExtendedTypes()) {
                extendNames.add(extendedType.getName().asString());
            }
            // 实现的接口名
            for (ClassOrInterfaceType implementedType : classOrInterfaceDeclaration.getImplementedTypes()) {
                implementedNames.add(implementedType.asString());
            }

            classInfo.setClassName(classOrInterfaceName);
            classInfo.setFilePath(fileName);
            classInfo.setExtendedList(extendNames);
            classInfo.setImplementedList(implementedNames);
            classInfo.setModifier(sb.toString());
            classInfo.setMethodInfos(parseMethod(classOrInterfaceDeclaration.findAll(MethodDeclaration.class)));

            // 解析构造函数
            //classInfo.getMethodInfos().addAll(parseConstructor(classOrInterfaceDeclaration.findAll(ConstructorDeclaration.class)));

            classInfo.setBegin(classOrInterfaceDeclaration.getBegin().get().line);
            classInfo.setEnd(classOrInterfaceDeclaration.getEnd().get().line);

            classInfos.add(classInfo);
        }
    }

    private List<MethodInfo> parseMethod(List<MethodDeclaration> methodDeclarations) {
        List<MethodInfo> methodInfos = new ArrayList<>();

        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            MethodInfo methodInfo = new MethodInfo();
            StringBuilder sb  = new StringBuilder();
            // name
            sb.append(methodDeclaration.getNameAsString());
            // parameters
            for (Parameter parameter : methodDeclaration.getParameters()) {
                sb.append(" ");
                sb.append(parameter.toString());
            }

            // signature
            methodInfo.setSignature(sb.toString());
            // modifier
            sb.setLength(0);
            for (Modifier modifier : methodDeclaration.getModifiers()) {
                sb.append(modifier.asString());
                sb.append(" ");
            }

            methodInfo.setBegin(methodDeclaration.getRange().get().begin.line);
            methodInfo.setEnd(methodDeclaration.getRange().get().end.line);
            methodInfo.setModifier(sb.toString());
            //primitiveType
            methodInfo.setPrimitiveType(methodDeclaration.getType().toString());
            methodInfos.add(methodInfo);
        }
        return methodInfos;
    }



    public String getPackageName() {
        return packageName;
    }

    public Set<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public String getFileName() {
        return fileName;
    }


}