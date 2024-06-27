package com.github.appreciated.plandex_plugin.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class EditorSelectionUtil {

    public static boolean isMethodSelected(@NotNull AnActionEvent e) {

        Editor editor = e.getData(DataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) return false;
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (element == null) return false;
        return PsiTreeUtil.getParentOfType(element.getParent(), PsiMethod.class) == null;
    }

    public static boolean isPsiMethod(PsiElement element) {
        if (element == null) {
            return false;
        }
        try {
            Method getNameMethod = element.getClass().getMethod("getName");
            return getNameMethod != null;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static String getPsiMethodName(PsiElement psiMethod) {
        try {
            Method getNameMethod = psiMethod.getClass().getMethod("getName");
            return (String) getNameMethod.invoke(psiMethod);
        } catch (Exception e) {
            e.printStackTrace();
            return "UnknownMethod";
        }
    }
}
