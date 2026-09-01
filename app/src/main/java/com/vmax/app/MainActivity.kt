.class public Lcom/tatkal/train/quick/MainActivity;
.super Landroidx/appcompat/app/AppCompatActivity;
.source "SourceFile"

# interfaces
.implements Ld4;


# static fields
.field public static p1:Ljava/lang/String;

.field public static q1:I

.field public static r1:Z


# instance fields
.field public A:[Lrl;

.field public A0:Ljava/util/Timer;

.field public B:Ljava/lang/String;

.field public B0:Lr91;

.field public C:I

.field public C0:Ljava/util/Timer;

.field public D:Z

.field public D0:Ljava/util/Timer;

.field public E:I

.field public E0:Lr91;

.field public F:Z

.field public F0:I

.field public G:Z

.field public G0:Ljava/lang/String;

.field public H:I

.field public H0:Ljava/lang/String;

.field public I:Z

.field public I0:Z

.field public J:Ljava/lang/String;

.field public J0:Z

.field public K:Z

.field public K0:Ljava/util/Timer;

.field public L:Ljava/lang/String;

.field public L0:Z

.field public M:Ljava/lang/String;

.field public M0:Ljava/lang/String;

.field public N:Ljava/lang/String;

.field public N0:Z

.field public O:Ljava/lang/String;

.field public final O0:Ljava/util/HashMap;

.field public P:Ljava/lang/String;

.field public final P0:Ljava/util/HashMap;

.field public Q:Ljava/lang/String;

.field public final Q0:Ljava/util/HashMap;

.field public R:Ljava/lang/String;

.field public R0:Z

.field public S:Ljava/lang/String;

.field public S0:J

.field public T:Ljava/lang/String;

.field public T0:Z

.field public U:Ljava/lang/String;

.field public U0:I

.field public V:I

.field public V0:Ljava/lang/String;

.field public W:I

.field public W0:I

.field public X:C

.field public X0:I

.field public Y:Ljava/lang/String;

.field public Y0:Lcom/tatkal/train/quick/OTPBroadcastReceiver;

.field public Z:Ljava/lang/String;

.field public Z0:Ljava/lang/String;

.field public a:Ljava/lang/String;

.field public a0:Ljava/lang/String;

.field public a1:I

.field public b:Ljava/lang/String;

.field public b0:Ljava/lang/String;

.field public b1:I

.field public c:Ljava/lang/String;

.field public c0:Ljava/lang/String;

.field public c1:I

.field public d:Ljava/lang/String;

.field public d0:Ljava/lang/String;

.field public d1:Z

.field public e:Ljava/lang/String;

.field public e0:Ljava/lang/String;

.field public e1:Landroid/widget/Button;

.field public f:Ljava/lang/String;

.field public f0:Ljava/lang/String;

.field public final f1:Ljava/util/HashMap;

.field public g0:Ljava/lang/String;

.field public g1:Landroid/app/ProgressDialog;

.field public h0:Ljava/lang/String;

.field public h1:I

.field public i0:Ljava/lang/String;

.field public i1:I

.field public j0:Ljava/lang/String;

.field public j1:Landroid/graphics/Bitmap;

.field public k0:Z

.field public k1:Lkf1;

.field public l0:Ljava/lang/String;

.field public l1:I

.field public m0:Z

.field public m1:Z

.field public n0:Ljava/lang/String;

.field public n1:Z

.field public o0:Ljava/lang/String;

.field public o1:I

.field public p0:Ljava/lang/String;

.field public q0:Z

.field public r0:Landroid/widget/ProgressBar;

.field public s:I

.field public s0:Lcom/tatkal/train/quick/AdvancedWebView;

.field public final t:Ljava/util/HashMap;

.field public t0:I

.field public u:Ljava/lang/String;

.field public u0:I

.field public v:Ljava/lang/String;

.field public v0:Ljava/util/Timer;

.field public w:Ljava/lang/String;

.field public w0:Ls91;

.field public x:I

.field public final x0:Landroid/os/Handler;

.field public y:I

.field public y0:Ljava/util/Timer;

.field public z:[Ldo1;

.field public z0:Ljava/util/Timer;


# direct methods
.method public constructor <init>()V
    .locals 3

    invoke-direct {p0}, Landroidx/appcompat/app/AppCompatActivity;-><init>()V

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->t:Ljava/util/HashMap;

    const/4 v0, 0x2

    new-array v0, v0, [Ldo1;

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->z:[Ldo1;

    const/4 v0, 0x0

    new-array v1, v0, [Lrl;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->A:[Lrl;

    const/4 v1, 0x1

    iput-boolean v1, p0, Lcom/tatkal/train/quick/MainActivity;->F:Z

    iput-boolean v1, p0, Lcom/tatkal/train/quick/MainActivity;->G:Z

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MainActivity;->I:Z

    const-string v1, ""

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->J:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->M:Ljava/lang/String;

    const-string v2, "MC"

    iput-object v2, p0, Lcom/tatkal/train/quick/MainActivity;->T:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->U:Ljava/lang/String;

    iput v0, p0, Lcom/tatkal/train/quick/MainActivity;->W:I

    const/16 v2, 0x52

    iput-char v2, p0, Lcom/tatkal/train/quick/MainActivity;->X:C

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->Y:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->Z:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->a0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->b0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->c0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->d0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->e0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->f0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->g0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->h0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->i0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->j0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->o0:Ljava/lang/String;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->p0:Ljava/lang/String;

    new-instance v2, Landroid/os/Handler;

    invoke-direct {v2}, Landroid/os/Handler;-><init>()V

    iput-object v2, p0, Lcom/tatkal/train/quick/MainActivity;->x0:Landroid/os/Handler;

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MainActivity;->I0:Z

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->O0:Ljava/util/HashMap;

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->P0:Ljava/util/HashMap;

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->Q0:Ljava/util/HashMap;

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->V0:Ljava/lang/String;

    const/4 v0, -0x1

    iput v0, p0, Lcom/tatkal/train/quick/MainActivity;->X0:I

    iput-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->Z0:Ljava/lang/String;

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->f1:Ljava/util/HashMap;

    return-void
.end method

.method public static A(ILjava/lang/String;)Ljava/lang/String;
    .locals 3

    new-instance v0, Ljava/lang/StringBuilder;

    const-string v1, "var url = \'\';var type = 0;var imgElem = document.getElementById(\'nlpCaptchaContainer\');if(imgElem != null && imgElem.getElementsByTagName(\'img\').length > 0) {url = imgElem.getElementsByTagName(\'img\')[imgElem.getElementsByTagName(\'img\').length - 1].src;} else if(document.getElementsByClassName(\'captcha-img\').length > 0) {url = document.getElementsByClassName(\'captcha-img\')[0].src;type = 1;}if(url != \'\') {if(url.startsWith(\'data\')) {if(document.getElementById(\'nlpAnswer\') == null || document.getElementById(\'nlpAnswer\').type != \'hidden\') {if("

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget v1, Lcom/tatkal/train/quick/SplashActivity;->u:I

    const/4 v2, 0x2

    if-eq v1, v2, :cond_1

    sget v1, Lcom/tatkal/train/quick/SplashActivity;->t:I

    if-nez v1, :cond_0

    sget-object v1, Ljg;->y:Ljava/lang/String;

    const-string v2, "FREE_USER"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_1

    sget-object v1, Ljg;->y:Ljava/lang/String;

    const-string v2, "COMP_USER"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v1, 0x1

    :goto_1
    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v1, ") {Step.solveCaptcha(url, type, "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ");"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "}}} else if(url.indexOf(\'nget\') > 0) {simulateClick(document.getElementsByClassName(\'glyphicon glyphicon-repeat\')[0].parentElement);}}"

    invoke-static {v0, p1, p0}, Lt30;->i(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public static B()Z
    .locals 11

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v0

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v1

    const/16 v2, 0x9

    const/16 v3, 0xb

    invoke-virtual {v1, v3, v2}, Ljava/util/Calendar;->set(II)V

    const/16 v2, 0xc

    const/16 v4, 0x37

    invoke-virtual {v1, v2, v4}, Ljava/util/Calendar;->set(II)V

    const/16 v5, 0xd

    const/4 v6, 0x0

    invoke-virtual {v1, v5, v6}, Ljava/util/Calendar;->set(II)V

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v7

    const/16 v8, 0xa

    invoke-virtual {v7, v3, v8}, Ljava/util/Calendar;->set(II)V

    const/16 v9, 0xf

    invoke-virtual {v7, v2, v9}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v7, v5, v6}, Ljava/util/Calendar;->set(II)V

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v10

    invoke-virtual {v10, v3, v8}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v10, v2, v4}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v10, v5, v6}, Ljava/util/Calendar;->set(II)V

    invoke-static {}, Ljava/util/Calendar;->getInstance()Ljava/util/Calendar;

    move-result-object v4

    invoke-virtual {v4, v3, v3}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v4, v2, v9}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v4, v5, v6}, Ljava/util/Calendar;->set(II)V

    invoke-virtual {v0, v1}, Ljava/util/Calendar;->after(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {v0, v7}, Ljava/util/Calendar;->before(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_1

    :cond_0
    invoke-virtual {v0, v10}, Ljava/util/Calendar;->after(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-virtual {v0, v4}, Ljava/util/Calendar;->before(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_2

    :cond_1
    const/4 v0, 0x1

    return v0

    :cond_2
    return v6
.end method

.method public static y(Lcom/tatkal/train/quick/MainActivity;)V
    .locals 3

    sget v0, Lhw1;->activity_main:I

    invoke-virtual {p0, v0}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    invoke-virtual {v0}, Landroid/view/View;->getHeight()I

    const/4 v1, 0x2

    new-array v1, v1, [I

    invoke-virtual {v0, v1}, Landroid/view/View;->getLocationOnScreen([I)V

    invoke-virtual {v0}, Landroid/view/View;->getRootView()Landroid/view/View;

    move-result-object v0

    invoke-virtual {v0}, Landroid/view/View;->getHeight()I

    move-result v0

    const/4 v2, 0x1

    aget v1, v1, v2

    sub-int/2addr v0, v1

    iget v1, p0, Lcom/tatkal/train/quick/MainActivity;->i1:I

    sub-int/2addr v1, v0

    const/16 v0, 0xc8

    if-le v1, v0, :cond_0

    invoke-super {p0}, Landroidx/activity/ComponentActivity;->onBackPressed()V

    :cond_0
    return-void
.end method

.method public static z(Lcom/tatkal/train/quick/MainActivity;ILjava/lang/String;I)V
    .locals 7

    const-string v0, ""

    invoke-virtual {p2, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const-string v2, "Error"

    if-eqz v1, :cond_0

    move-object p2, v2

    :cond_0
    const/4 v1, 0x0

    const/4 v3, 0x1

    if-nez p3, :cond_1

    iget v4, p0, Lcom/tatkal/train/quick/MainActivity;->a1:I

    goto :goto_0

    :cond_1
    if-ne p3, v3, :cond_2

    iget v4, p0, Lcom/tatkal/train/quick/MainActivity;->b1:I

    goto :goto_0

    :cond_2
    move v4, v1

    :goto_0
    if-le v4, v3, :cond_3

    const-wide/16 v5, 0x3e8

    :try_start_0
    invoke-static {v5, v6}, Ljava/lang/Thread;->sleep(J)V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    :cond_3
    const/4 v5, 0x3

    if-lt v4, v5, :cond_4

    invoke-virtual {p2, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_4

    move-object p2, v0

    :cond_4
    const/4 v2, 0x2

    if-ne p1, v2, :cond_5

    const-wide/16 v5, 0x1f5

    :try_start_1
    invoke-static {v5, v6}, Ljava/lang/Thread;->sleep(J)V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    :catch_1
    :cond_5
    const-string v5, "ERROR"

    invoke-virtual {p2, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_8

    if-nez p1, :cond_6

    iget-object p1, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const-string v1, "\';document.getElementById(\'nlpAnswer\').focus();Step.spaceAndBackspace();exceed = Step.triesExceeded("

    const-string v2, ");if(exceed == 2) {return;} setTimeout(function() {if(document.getElementsByClassName(\'train_Search\').length > 1) {if(exceed != 1) {simulateClick(document.getElementsByClassName(\'search_btn train_Search\')[2]);} else {document.getElementById(\'nlpAnswer\').focus();}var captchaCheck = setInterval(function() {if(document.getElementsByClassName(\'loginError\')[0].innerHTML != \'\' && ((document.getElementById(\'nlpAnswer\') != null && document.getElementById(\'nlpCaptchaContainer\') != null && document.getElementById(\'nlpCaptchaContainer\').getElementsByTagName(\'img\').length > 0) || (document.getElementById(\'captcha\') != null && document.getElementsByClassName(\'captcha-img\').length > 0))) {clearInterval(captchaCheck);"

    const-string v3, "javascript:var exceed = 0;function simulateClick(element) {\n  if (element && typeof element.dispatchEvent === \'function\') {\n      if (typeof element.focus === \'function\') {\n          try {\n              element.focus();\n          } catch (e) {\n              console.warn(\'Could not focus element before click:\',\n                  element,\n                  e.message\n              );\n          }\n      }\n      if (element.disabled) {\n          console.warn(\'Attempted to click on a disabled element:\', element);\n          return;\n      }\n      const mouseDown = new MouseEvent(\'mousedown\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          }),\n          mouseUp = new MouseEvent(\'mouseup\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          }),\n          mouseClick = new MouseEvent(\'click\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          });\n      element.dispatchEvent(mouseDown);\n      element.dispatchEvent(mouseUp);\n      element.dispatchEvent(mouseClick);\n  } else {\n      console.warn(\'Attempted to simulate click on an invalid (null, undefined, or no dispatchEvent method) element:\', element);\n  }\n}function fill() {document.getElementById(\'nlpAnswer\').value = \'"

    invoke-static {v4, v3, p2, v1, v2}, Lyi;->i(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p2

    invoke-static {p3, v0}, Lcom/tatkal/train/quick/MainActivity;->A(ILjava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "}}, 100); } else {if(exceed != 1) {simulateClick(document.getElementsByClassName(\'train_Search btnDefault\')[0]);} else {document.getElementById(\'nlpAnswer\').focus();}var captchaCheck = setInterval(function() {if(document.getElementsByTagName(\'p-toastitem\').length > 0 && ((document.getElementById(\'nlpAnswer\') != null && document.getElementById(\'nlpCaptchaContainer\') != null && document.getElementById(\'nlpCaptchaContainer\').getElementsByTagName(\'img\').length > 0) || (document.getElementById(\'captcha\') != null && document.getElementsByClassName(\'captcha-img\').length > 0))) {try {\n    document.getElementsByTagName(\'p-toastitem\')[0].getElementsByTagName(\'a\')[0].click();\n} catch {}clearInterval(captchaCheck);"

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p3, v0}, Lcom/tatkal/train/quick/MainActivity;->A(ILjava/lang/String;)Ljava/lang/String;

    move-result-object p3

    invoke-virtual {p2, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p3, "}}, 100); }}, "

    invoke-virtual {p2, p3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p0, p0, Lcom/tatkal/train/quick/MainActivity;->o1:I

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ");}fill()"

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p1, p0}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;)V

    goto :goto_1

    :cond_6
    if-ne p1, v3, :cond_7

    iget-object p0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const-string p1, "\') {capElement.focus();setTimeout(function() {Step.pressKey(\'"

    const-string v1, "\');}, 100);}})();exceed = Step.triesExceeded("

    const-string v2, "javascript:var exceed = 0;async function simulateBackspaceClear(el, delay = 5) {\n  el.focus();\n  const length = el.value.length;\n\n  for (let i = 0; i < length; i++) {\n    el.dispatchEvent(new KeyboardEvent(\'keydown\', {\n      key: \'Backspace\',\n      code: \'Backspace\',\n      bubbles: true,\n      cancelable: true\n    }));\n\n    el.dispatchEvent(new KeyboardEvent(\'keypress\', {\n      key: \'Backspace\',\n      code: \'Backspace\',\n      bubbles: true,\n      cancelable: true\n    }));\n\n    el.value = el.value.slice(0, -1);\n\n    el.dispatchEvent(new InputEvent(\'input\', {\n      inputType: \'deleteContentBackward\',\n      data: null,\n      bubbles: true,\n      cancelable: true\n    }));\n\n    el.dispatchEvent(new KeyboardEvent(\'keyup\', {\n      key: \'Backspace\',\n      code: \'Backspace\',\n      bubbles: true,\n      cancelable: true\n    }));\n\n    await new Promise(r => setTimeout(r, delay));\n  }\n\n  el.dispatchEvent(new Event(\'change\', { bubbles: true }));\n}\nfunction simulateClick(element) {\n  if (element && typeof element.dispatchEvent === \'function\') {\n      if (typeof element.focus === \'function\') {\n          try {\n              element.focus();\n          } catch (e) {\n              console.warn(\'Could not focus element before click:\',\n                  element,\n                  e.message\n              );\n          }\n      }\n      if (element.disabled) {\n          console.warn(\'Attempted to click on a disabled element:\', element);\n          return;\n      }\n      const mouseDown = new MouseEvent(\'mousedown\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          }),\n          mouseUp = new MouseEvent(\'mouseup\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          }),\n          mouseClick = new MouseEvent(\'click\', {\n              bubbles: true,\n              cancelable: true,\n              view: window,\n              button: 0,\n          });\n      element.dispatchEvent(mouseDown);\n      element.dispatchEvent(mouseUp);\n      element.dispatchEvent(mouseClick);\n  } else {\n      console.warn(\'Attempted to simulate click on an invalid (null, undefined, or no dispatchEvent method) element:\', element);\n  }\n}function fill() {var capElement = document.getElementById(\'captcha\');(async () => { if(capElement.value != \'"

    invoke-static {v2, p2, p1, p2, v1}, Lt30;->o(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object p1

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ");if(exceed == 2) {return;} setTimeout(function() {if(document.getElementsByClassName(\'train_Search\').length > 1) {if(exceed != 1) {setTimeout(function() {if(document.getElementById(\'captcha\').value != \'"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\') {setText(document.getElementById(\'captcha\'), \'"

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "\');}Step.print(\'Submitting CAPTCHA\');simulateClick(document.getElementsByClassName(\'search_btn train_Search\')[2]);}, 1000);} else {document.getElementById(\'captcha\').focus();}var captchaCheck = setInterval(function() {if(document.getElementsByClassName(\'loginError\')[0].innerHTML != \'\' && ((document.getElementById(\'nlpCaptchaContainer\') != null && document.getElementById(\'nlpCaptchaContainer\').getElementsByTagName(\'img\').length > 0) || document.getElementsByClassName(\'captcha-img\').length > 0)) {clearInterval(captchaCheck);"

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p3, v0}, Lcom/tatkal/train/quick/MainActivity;->A(ILjava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, "}}, 100); } else {if(exceed != 1) {setTimeout(function() {if(document.getElementById(\'captcha\').value != \'"

    invoke-static {p1, v2, p2, v1, p2}, Lq90;->t(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    const-string p2, "\');}Step.print(\'Submitting CAPTCHA\');document.getElementById(\'captcha\').blur();simulateClick(document.getElementsByClassName(\'train_Search btnDefault\')[0]);}, 1200);} else {document.getElementById(\'captcha\').focus();}var captchaCheck = setInterval(function() {if(document.getElementsByTagName(\'p-toastitem\').length > 0 && ((document.getElementById(\'nlpAnswer\') != null && document.getElementById(\'nlpCaptchaContainer\') != null && document.getElementById(\'nlpCaptchaContainer\').getElementsByTagName(\'img\').length > 0) || (document.getElementById(\'captcha\') != null && document.getElementsByClassName(\'captcha-img\').length > 0))) {try {\n    document.getElementsByTagName(\'p-toastitem\')[0].getElementsByTagName(\'a\')[0].click();\n} catch {}clearInterval(captchaCheck);"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p3, v0}, Lcom/tatkal/train/quick/MainActivity;->A(ILjava/lang/String;)Ljava/lang/String;

    move-result-object p2

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "}}, 100); }}, 101);}fill()"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;)V

    goto :goto_1

    :cond_7
    if-ne p1, v2, :cond_9

    const-string p1, "STUDIOS"

    const-string p3, "SUBMITTING HDFC CAPTCHA"

    invoke-static {p1, p3}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance p1, Ljava/lang/StringBuilder;

    const-string p3, "javascript:function aish() {document.getElementsByName(\'passline\')[0].value = \'"

    invoke-direct {p1, p3}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, "\';\ndocument.getElementById(\'submit_btn\').click();}aish()"

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;)V

    goto :goto_1

    :cond_8
    const-string p1, "Error! Please fill captcha manually"

    invoke-static {p0, p1, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object p0

    invoke-virtual {p0}, Landroid/widget/Toast;->show()V

    :cond_9
    :goto_1
    return-void
.end method


# virtual methods
.method public final C()V
    .locals 4

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->v:Ljava/lang/String;

    const-string v1, "2S"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->v:Ljava/lang/String;

    const-string v1, "FC"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->v:Ljava/lang/String;

    const-string v1, "SL"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/16 v0, 0xa

    goto :goto_1

    :cond_1
    :goto_0
    const/16 v0, 0xb

    :goto_1
    sget v1, Lhw1;->activity_main:I

    invoke-virtual {p0, v1}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object p0

    check-cast p0, Landroid/widget/RelativeLayout;

    const-string v1, ""

    const/4 v2, -0x2

    invoke-static {p0, v1, v2}, Lcom/google/android/material/snackbar/Snackbar;->make(Landroid/view/View;Ljava/lang/CharSequence;I)Lcom/google/android/material/snackbar/Snackbar;

    move-result-object p0

    new-instance v1, Lsu;

    const/4 v2, 0x3

    invoke-direct {v1, v2}, Lsu;-><init>(I)V

    const-string v2, "OK"

    invoke-virtual {p0, v2, v1}, Lcom/google/android/material/snackbar/Snackbar;->setAction(Ljava/lang/CharSequence;Landroid/view/View$OnClickListener;)Lcom/google/android/material/snackbar/Snackbar;

    move-result-object p0

    const/16 v1, -0x100

    invoke-virtual {p0, v1}, Lcom/google/android/material/snackbar/Snackbar;->setActionTextColor(I)Lcom/google/android/material/snackbar/Snackbar;

    invoke-virtual {p0}, Lcom/google/android/material/snackbar/BaseTransientBottomBar;->getView()Landroid/view/View;

    move-result-object v1

    sget v2, Lcom/google/android/material/R$id;->snackbar_text:I

    invoke-virtual {v1, v2}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v3, "Please don\'t refresh availability before "

    invoke-direct {v2, v3}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, ":00 AM to avoid logout"

    invoke-virtual {v2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const/4 v2, 0x0

    invoke-static {v0, v2}, Landroid/text/Html;->fromHtml(Ljava/lang/String;I)Landroid/text/Spanned;

    move-result-object v0

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    const/4 v0, 0x7

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setMaxLines(I)V

    const/high16 v0, 0x41600000    # 14.0f

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setTextSize(F)V

    invoke-virtual {p0}, Lcom/google/android/material/snackbar/Snackbar;->show()V

    return-void
.end method

.method public final attachBaseContext(Landroid/content/Context;)V
    .locals 3

    invoke-static {p1}, Landroid/preference/PreferenceManager;->getDefaultSharedPreferences(Landroid/content/Context;)Landroid/content/SharedPreferences;

    move-result-object v0

    const-string v1, "OPTION"

    const/4 v2, 0x0

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v0

    sget-object v1, Lcom/tatkal/train/quick/FormActivity2;->z:[Ljava/lang/String;

    aget-object v0, v1, v0

    invoke-static {p1, v0}, Lsh1;->a(Landroid/content/Context;Ljava/lang/String;)Lsh1;

    move-result-object p1

    invoke-super {p0, p1}, Landroidx/appcompat/app/AppCompatActivity;->attachBaseContext(Landroid/content/Context;)V

    return-void
.end method

.method public final onActivityResult(IILandroid/content/Intent;)V
    .locals 0

    invoke-super {p0, p1, p2, p3}, Landroidx/fragment/app/FragmentActivity;->onActivityResult(IILandroid/content/Intent;)V

    iget-object p0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {p0, p1, p2, p3}, Lcom/tatkal/train/quick/AdvancedWebView;->c(IILandroid/content/Intent;)V

    return-void
.end method

.method public final onBackPressed()V
    .locals 7

    iget-boolean v0, p0, Lcom/tatkal/train/quick/MainActivity;->I0:Z

    if-eqz v0, :cond_0

    invoke-super {p0}, Landroidx/activity/ComponentActivity;->onBackPressed()V

    return-void

    :cond_0
    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Landroid/webkit/WebView;->canGoBack()Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Landroid/webkit/WebView;->goBack()V

    :cond_1
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/tatkal/train/quick/MainActivity;->I0:Z

    const-string v0, "Press one more time to exit"

    const/4 v1, 0x0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    new-instance v0, Ljava/util/Timer;

    invoke-direct {v0}, Ljava/util/Timer;-><init>()V

    iput-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->z0:Ljava/util/Timer;

    new-instance v0, Landroid/os/Handler;

    invoke-direct {v0}, Landroid/os/Handler;-><init>()V

    new-instance v2, Lq91;

    const/4 v1, 0x2

    invoke-direct {v2, p0, v0, v1}, Lq91;-><init>(Lcom/tatkal/train/quick/MainActivity;Landroid/os/Handler;I)V

    iget-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->z0:Ljava/util/Timer;

    const-wide/16 v3, 0xfa0

    const-wide/16 v5, 0x2710

    invoke-virtual/range {v1 .. v6}, Ljava/util/Timer;->schedule(Ljava/util/TimerTask;JJ)V

    return-void
.end method

.method public final onCreate(Landroid/os/Bundle;)V
    .locals 52

    move-object/from16 v0, p0

    invoke-super/range {p0 .. p1}, Landroidx/fragment/app/FragmentActivity;->onCreate(Landroid/os/Bundle;)V

    invoke-static {v0}, Lcom/google/firebase/analytics/FirebaseAnalytics;->getInstance(Landroid/content/Context;)Lcom/google/firebase/analytics/FirebaseAnalytics;

    sget v1, Lpw1;->activity_main:I

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->setContentView(I)V

    sget v1, Lhw1;->toolbar:I

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroidx/appcompat/widget/Toolbar;

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->setSupportActionBar(Landroidx/appcompat/widget/Toolbar;)V

    const/4 v1, 0x0

    sput-boolean v1, Lcom/tatkal/train/quick/MainActivity;->r1:Z

    sget v2, Lhw1;->activity_main:I

    invoke-virtual {v0, v2}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v2

    invoke-virtual {v2}, Landroid/view/View;->getHeight()I

    const/4 v3, 0x2

    new-array v4, v3, [I

    invoke-virtual {v2, v4}, Landroid/view/View;->getLocationOnScreen([I)V

    invoke-virtual {v2}, Landroid/view/View;->getRootView()Landroid/view/View;

    move-result-object v2

    invoke-virtual {v2}, Landroid/view/View;->getHeight()I

    move-result v2

    const/4 v5, 0x1

    aget v4, v4, v5

    sub-int/2addr v2, v4

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->i1:I

    sget-object v2, Lcom/tatkal/train/quick/QuickTatkalApp;->a:Lkf1;

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->k1:Lkf1;

    new-instance v2, Lyz0;

    invoke-direct {v2}, Lyz0;-><init>()V

    :try_start_0
    const-string v4, "Quota"

    iget-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->f:Ljava/lang/String;

    invoke-virtual {v2, v6, v4}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v4, "Payment method"

    iget-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    invoke-virtual {v2, v6, v4}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v4, "Bank"

    sget-object v6, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    invoke-virtual {v2, v6, v4}, Lyz0;->z(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->k1:Lkf1;

    const-string v6, "Start website booking"

    invoke-virtual {v4, v2, v6}, Lkf1;->l(Lyz0;Ljava/lang/String;)V
    :try_end_0
    .catch Lxz0; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    new-instance v2, Ljava/util/Timer;

    invoke-direct {v2}, Ljava/util/Timer;-><init>()V

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->y0:Ljava/util/Timer;

    new-instance v2, Landroid/os/Handler;

    invoke-direct {v2}, Landroid/os/Handler;-><init>()V

    new-instance v7, Lq91;

    invoke-direct {v7, v0, v2, v1}, Lq91;-><init>(Lcom/tatkal/train/quick/MainActivity;Landroid/os/Handler;I)V

    iget-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->y0:Ljava/util/Timer;

    const-wide/16 v8, 0x0

    const-wide/16 v10, 0x3e8

    invoke-virtual/range {v6 .. v11}, Ljava/util/Timer;->schedule(Ljava/util/TimerTask;JJ)V

    new-instance v2, Lcom/tatkal/train/quick/OTPBroadcastReceiver;

    invoke-direct {v2}, Lcom/tatkal/train/quick/OTPBroadcastReceiver;-><init>()V

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->Y0:Lcom/tatkal/train/quick/OTPBroadcastReceiver;

    new-instance v2, Landroid/content/IntentFilter;

    const-string v4, "com.quickotp.OTPReceived"

    invoke-direct {v2, v4}, Landroid/content/IntentFilter;-><init>(Ljava/lang/String;)V

    sget v4, Landroid/os/Build$VERSION;->SDK_INT:I

    iget-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->Y0:Lcom/tatkal/train/quick/OTPBroadcastReceiver;

    const/16 v7, 0x22

    if-lt v4, v7, :cond_0

    invoke-virtual {v0, v6, v2, v3}, Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;I)Landroid/content/Intent;

    goto :goto_0

    :cond_0
    invoke-virtual {v0, v6, v2}, Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;

    :goto_0
    invoke-virtual {v0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Intent;->getExtras()Landroid/os/Bundle;

    move-result-object v2

    const-string v4, "FORM_NAME"

    invoke-virtual {v2, v4}, Landroid/os/BaseBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    invoke-virtual {v0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Intent;->getExtras()Landroid/os/Bundle;

    move-result-object v2

    const-string v6, "LANG"

    invoke-virtual {v2, v6}, Landroid/os/BaseBundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->M0:Ljava/lang/String;

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const-string v6, ""

    if-nez v2, :cond_1

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    const-string v8, "RC"

    invoke-virtual {v2, v8, v1}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v2

    invoke-interface {v2, v4, v6}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    :cond_1
    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    iput-boolean v1, v0, Lcom/tatkal/train/quick/MainActivity;->T0:Z

    const-string v4, "GN"

    iget-object v8, v0, Lcom/tatkal/train/quick/MainActivity;->t:Ljava/util/HashMap;

    invoke-virtual {v8, v4, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    const-string v9, "LD"

    invoke-virtual {v8, v9, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v9

    const-string v10, "SS"

    invoke-virtual {v8, v10, v9}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const/4 v10, 0x3

    invoke-static {v10}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v11

    const-string v12, "HP"

    invoke-virtual {v8, v12, v11}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const/4 v12, 0x4

    invoke-static {v12}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v13

    const-string v14, "DP"

    invoke-virtual {v8, v14, v13}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const/4 v14, 0x5

    invoke-static {v14}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v15

    const-string v7, "TQ"

    invoke-virtual {v8, v7, v15}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v7, "PT"

    const/16 v16, 0x6

    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-virtual {v8, v7, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Debit Card with PIN"

    iget-object v7, v0, Lcom/tatkal/train/quick/MainActivity;->Q0:Ljava/util/HashMap;

    const-string v8, "DEBIT_CARD"

    invoke-virtual {v7, v8, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Netbanking"

    const-string v14, "NETBANKING"

    invoke-virtual {v7, v14, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Bharat QR / Scan & Pay"

    const-string v12, "SCAN_AND_PAY"

    invoke-virtual {v7, v12, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Wallets / Cash Card"

    const-string v10, "CASH_CARD"

    invoke-virtual {v7, v10, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Multiple Payment Service"

    const-string v5, "MULTIPLE_GATEWAY"

    invoke-virtual {v7, v5, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "IRCTC eWallet"

    const-string v3, "E_WALLET"

    invoke-virtual {v7, v3, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Pay-On-Delivery/Pay later"

    move-object/from16 v18, v8

    const-string v8, "COD"

    invoke-virtual {v7, v8, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "Payment Gateway / Credit Card / Debit Card"

    move-object/from16 v19, v8

    const-string v8, "CREDIT_CARD"

    invoke-virtual {v7, v8, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v1, "IRCTC_IPAY"

    move-object/from16 v20, v12

    const-string v12, "IRCTC iPay (Credit Card/Debit Card/UPI)"

    invoke-virtual {v7, v1, v12}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-object/from16 v21, v7

    iget-object v7, v0, Lcom/tatkal/train/quick/MainActivity;->O0:Ljava/util/HashMap;

    invoke-virtual {v7, v1, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v5, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v14, v9}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v10, v13}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v3, v15}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v8, v11}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v7, v0, Lcom/tatkal/train/quick/MainActivity;->P0:Ljava/util/HashMap;

    const-string v9, "UPI_VPA"

    invoke-virtual {v7, v9, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {v7, v5, v4}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    new-instance v2, Lag;

    const/4 v4, 0x2

    invoke-direct {v2, v0, v4}, Lag;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v4

    new-instance v7, Ljava/lang/StringBuilder;

    const-string v11, "select * from BOOKING_INFO where FORM_NAME = \'"

    invoke-direct {v7, v11}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v11, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const-string v13, "\'"

    const/4 v15, 0x0

    invoke-static {v7, v11, v13, v4, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v7

    invoke-static {}, Lzo0;->g()[B

    move-result-object v11

    invoke-interface {v7}, Landroid/database/Cursor;->moveToNext()Z

    move-result v22

    const-string v15, "1"

    move-object/from16 v23, v2

    move-object/from16 v24, v4

    if-eqz v22, :cond_1f

    const/4 v2, 0x1

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    const-string v2, " "

    invoke-virtual {v4, v2, v6}, Ljava/lang/String;->replaceAll(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->a:Ljava/lang/String;

    const/4 v4, 0x2

    invoke-interface {v7, v4}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    new-instance v4, Ljava/lang/String;

    invoke-direct {v4, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->b:Ljava/lang/String;

    const/4 v2, 0x3

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->c:Ljava/lang/String;

    const/4 v2, 0x4

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->d:Ljava/lang/String;

    const/4 v2, 0x5

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->e:Ljava/lang/String;

    move/from16 v2, v16

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->f:Ljava/lang/String;

    const-string v2, "FARE_LIMIT"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v2

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->s:I

    const-string v2, "CLICK"

    const/4 v4, 0x0

    invoke-virtual {v0, v2, v4}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v2

    const-string v6, "VALUE"

    invoke-interface {v2, v6, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v2

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->x:I

    const/4 v2, 0x7

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->v:Ljava/lang/String;

    const/16 v2, 0x8

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->u:Ljava/lang/String;

    const/16 v2, 0x9

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->w:Ljava/lang/String;

    const-string v2, "REFRESH_SEC"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v2

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->y:I

    const/16 v2, 0xa

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->J:Ljava/lang/String;

    const/16 v2, 0xb

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v2

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->I:Z

    const/16 v2, 0xc

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    const/16 v2, 0xd

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v2

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->F:Z

    const/16 v2, 0xe

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v2

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->G:Z

    const/16 v2, 0xf

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v4

    iput v4, v0, Lcom/tatkal/train/quick/MainActivity;->H:I

    const/16 v2, 0x10

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->B:Ljava/lang/String;

    const/16 v2, 0x11

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    const/16 v2, 0x12

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v2

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->K:Z

    const-string v2, "DELAY_SEC"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v2

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->V:I

    const-string v2, "CAPTCHA_AUTOFILL"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_2

    const/4 v2, 0x1

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->D:Z

    sget-object v2, Lk6;->r:Ljava/lang/String;

    const-string v4, "N"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->d1:Z

    :cond_2
    const-string v2, "AUTO_OPEN"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2, v15}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_3

    const/4 v2, 0x1

    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->m0:Z

    :cond_3
    :try_start_1
    const-string v2, "WB_PYMT_MODE"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v2

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->C:I

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    if-eqz v2, :cond_4

    invoke-virtual {v2, v9}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_4

    const/4 v2, 0x1

    iput v2, v0, Lcom/tatkal/train/quick/MainActivity;->C:I
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    :catch_1
    :cond_4
    new-instance v2, Lz3;

    const/4 v4, 0x3

    invoke-direct {v2, v0, v4}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v6

    new-instance v4, Ljava/lang/StringBuilder;

    move-object/from16 v25, v2

    const-string v2, "select * from GST_INFO_TBL where FORM_NAME = \'"

    invoke-direct {v4, v2}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    move-object/from16 v26, v15

    const/4 v15, 0x0

    invoke-static {v4, v2, v13, v6, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v2

    invoke-interface {v2}, Landroid/database/Cursor;->moveToNext()Z

    move-result v4

    if-eqz v4, :cond_7

    const/4 v4, 0x1

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v4, 0x2

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v4, 0x3

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v4, 0x4

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v15

    if-eqz v15, :cond_5

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    :cond_5
    const/4 v4, 0x5

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v4, 0x6

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v15

    if-eqz v15, :cond_6

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    :cond_6
    const/4 v4, 0x7

    invoke-interface {v2, v4}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    :cond_7
    invoke-interface {v2}, Landroid/database/Cursor;->close()V

    invoke-interface {v2}, Landroid/database/Cursor;->close()V

    invoke-virtual {v6}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual/range {v25 .. v25}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    new-instance v2, Lz3;

    const/4 v4, 0x0

    invoke-direct {v2, v0, v4}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v4

    new-instance v6, Ljava/lang/StringBuilder;

    const-string v15, "select * from ADDRESS_TBL where FORM_NAME = \'"

    invoke-direct {v6, v15}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v15, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    move-object/from16 v25, v2

    const/4 v2, 0x0

    invoke-static {v6, v15, v13, v4, v2}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v6

    invoke-interface {v6}, Landroid/database/Cursor;->moveToNext()Z

    move-result v2

    if-eqz v2, :cond_8

    const-string v2, "ADDR1"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v2, "ADDR2"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v2, "ADDR3"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v2, "PIN"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v2, "CITY"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const-string v2, "PO"

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v6, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    :cond_8
    invoke-interface {v6}, Landroid/database/Cursor;->close()V

    invoke-virtual {v4}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual/range {v25 .. v25}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    if-eqz v2, :cond_a

    move-object/from16 v4, v20

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_9

    const-string v2, "Bharat QR(powered by Atom)"

    sput-object v2, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto :goto_1

    :cond_9
    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    const-string v4, "IRCTC_PREPAID"

    invoke-virtual {v2, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_a

    const-string v2, "IRCTC Union Bank prepaid (RuPay)"

    sput-object v2, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    :cond_a
    :goto_1
    const/16 v2, 0x2f

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    const/16 v4, 0x2c

    invoke-interface {v7, v4}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v4

    invoke-static {v11, v4}, Lzo0;->c([B[B)[B

    move-result-object v4

    const/16 v6, 0x13

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v6

    invoke-static {v11, v6}, Lzo0;->c([B[B)[B

    move-result-object v6

    const/16 v15, 0x14

    invoke-interface {v7, v15}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v15

    invoke-static {v11, v15}, Lzo0;->c([B[B)[B

    move-result-object v15

    move-object/from16 v20, v13

    const/16 v13, 0x15

    invoke-interface {v7, v13}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v13

    invoke-static {v11, v13}, Lzo0;->c([B[B)[B

    move-result-object v13

    move-object/from16 v25, v12

    const/16 v12, 0x16

    invoke-interface {v7, v12}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v12

    invoke-static {v11, v12}, Lzo0;->c([B[B)[B

    move-result-object v12

    move-object/from16 v27, v4

    const/16 v4, 0x20

    invoke-interface {v7, v4}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v4

    invoke-static {v11, v4}, Lzo0;->c([B[B)[B

    move-result-object v4

    move-object/from16 v28, v1

    const/16 v1, 0x1f

    move-object/from16 v29, v3

    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v3

    invoke-static {v11, v3}, Lzo0;->c([B[B)[B

    move-result-object v3

    const/16 v1, 0x1d

    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v1

    invoke-static {v11, v1}, Lzo0;->c([B[B)[B

    move-result-object v1

    move-object/from16 v30, v5

    const/16 v5, 0x1e

    move-object/from16 v31, v9

    invoke-interface {v7, v5}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v9

    invoke-static {v11, v9}, Lzo0;->c([B[B)[B

    move-result-object v9

    const/16 v5, 0x21

    invoke-interface {v7, v5}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v5

    invoke-static {v11, v5}, Lzo0;->c([B[B)[B

    move-result-object v5

    move-object/from16 v32, v8

    const/16 v8, 0x22

    invoke-interface {v7, v8}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v8

    invoke-static {v11, v8}, Lzo0;->c([B[B)[B

    move-result-object v8

    move-object/from16 p1, v1

    const/16 v1, 0x19

    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v1

    invoke-static {v11, v1}, Lzo0;->c([B[B)[B

    move-result-object v1

    move-object/from16 v33, v14

    const/16 v14, 0x1a

    invoke-interface {v7, v14}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v14

    invoke-static {v11, v14}, Lzo0;->c([B[B)[B

    move-result-object v14

    move-object/from16 v34, v1

    const/16 v1, 0x1b

    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v1

    invoke-static {v11, v1}, Lzo0;->c([B[B)[B

    move-result-object v1

    move-object/from16 v35, v10

    const/16 v10, 0x1c

    invoke-interface {v7, v10}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v10

    invoke-static {v11, v10}, Lzo0;->c([B[B)[B

    move-result-object v10

    move-object/from16 v36, v2

    const/16 v2, 0x2d

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v37, v2

    const/16 v2, 0x2e

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v38, v2

    const/16 v2, 0x23

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v39, v2

    const/16 v2, 0x24

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v40, v2

    const/16 v2, 0x25

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v41, v2

    const/16 v2, 0x26

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v42, v2

    const/16 v2, 0x27

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v43, v2

    const/16 v2, 0x28

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v44, v2

    const/16 v2, 0x29

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v45, v2

    const/16 v2, 0x2a

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v46, v2

    const/16 v2, 0x2b

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v47, v2

    const-string v2, "UPI_BANK"

    invoke-static {v7, v2, v11}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v2

    move-object/from16 v48, v2

    const/16 v2, 0x32

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v49, v2

    const/16 v2, 0x33

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v2

    invoke-static {v11, v2}, Lzo0;->c([B[B)[B

    move-result-object v2

    move-object/from16 v50, v2

    const-string v2, "QR_PYMT"

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v7, v2}, Landroid/database/Cursor;->getInt(I)I

    move-result v2

    move-object/from16 v51, v8

    const/4 v8, 0x1

    if-ne v2, v8, :cond_b

    const/4 v2, 0x1

    goto :goto_2

    :cond_b
    const/4 v2, 0x0

    :goto_2
    iput-boolean v2, v0, Lcom/tatkal/train/quick/MainActivity;->k0:Z

    const-string v2, "MULTIPLE_PYMT_CHOICE"

    invoke-static {v7, v2, v11}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v2

    new-instance v8, Ljava/lang/String;

    invoke-direct {v8, v6}, Ljava/lang/String;-><init>([B)V

    iput-object v8, v0, Lcom/tatkal/train/quick/MainActivity;->N:Ljava/lang/String;

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v15}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->P:Ljava/lang/String;

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v13}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->O:Ljava/lang/String;

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v12}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->S:Ljava/lang/String;

    const/16 v6, 0x17

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v8

    if-eqz v8, :cond_c

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v6

    invoke-static {v11, v6}, Lzo0;->c([B[B)[B

    move-result-object v6

    new-instance v8, Ljava/lang/String;

    invoke-direct {v8, v6}, Ljava/lang/String;-><init>([B)V

    iput-object v8, v0, Lcom/tatkal/train/quick/MainActivity;->R:Ljava/lang/String;

    :cond_c
    const/16 v6, 0x18

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v8

    if-eqz v8, :cond_d

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v6

    invoke-static {v11, v6}, Lzo0;->c([B[B)[B

    move-result-object v6

    new-instance v8, Ljava/lang/String;

    invoke-direct {v8, v6}, Ljava/lang/String;-><init>([B)V

    iput-object v8, v0, Lcom/tatkal/train/quick/MainActivity;->Q:Ljava/lang/String;

    :cond_d
    const-string v6, "STATIC_PASS"

    invoke-interface {v7, v6}, Landroid/database/Cursor;->getColumnIndex(Ljava/lang/String;)I

    move-result v8

    invoke-interface {v7, v8}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v8

    if-eqz v8, :cond_e

    invoke-static {v7, v6, v11}, Lp91;->q(Landroid/database/Cursor;Ljava/lang/String;[B)[B

    move-result-object v6

    new-instance v8, Ljava/lang/String;

    invoke-direct {v8, v6}, Ljava/lang/String;-><init>([B)V

    iput-object v8, v0, Lcom/tatkal/train/quick/MainActivity;->U:Ljava/lang/String;

    :cond_e
    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v14}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->o0:Ljava/lang/String;

    if-eqz v1, :cond_f

    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v1}, Ljava/lang/String;-><init>([B)V

    iput-object v6, v0, Lcom/tatkal/train/quick/MainActivity;->p0:Ljava/lang/String;

    :cond_f
    new-instance v1, Ljava/lang/String;

    invoke-direct {v1, v10}, Ljava/lang/String;-><init>([B)V

    const/16 v1, 0x1e

    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v1

    if-eqz v1, :cond_10

    const/16 v1, 0x52

    :try_start_2
    new-instance v6, Ljava/lang/String;

    invoke-direct {v6, v9}, Ljava/lang/String;-><init>([B)V

    const/4 v8, 0x0

    invoke-virtual {v6, v8}, Ljava/lang/String;->charAt(I)C

    move-result v6

    iput-char v6, v0, Lcom/tatkal/train/quick/MainActivity;->X:C

    const/16 v8, 0x50

    if-ne v6, v8, :cond_10

    iput-char v1, v0, Lcom/tatkal/train/quick/MainActivity;->X:C
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_2

    :cond_10
    :goto_3
    const/16 v1, 0x1f

    goto :goto_4

    :catch_2
    iput-char v1, v0, Lcom/tatkal/train/quick/MainActivity;->X:C

    goto :goto_3

    :goto_4
    invoke-interface {v7, v1}, Landroid/database/Cursor;->getBlob(I)[B

    move-result-object v1

    if-eqz v1, :cond_11

    new-instance v1, Ljava/lang/String;

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    iput v1, v0, Lcom/tatkal/train/quick/MainActivity;->W:I

    :cond_11
    new-instance v1, Ljava/lang/String;

    invoke-direct {v1, v4}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->Y:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    invoke-direct {v1, v5}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->Z:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v51

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->a0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v41

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->d0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v42

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->e0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v43

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->f0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v44

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->g0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v45

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->h0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v46

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->i0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v47

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->j0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v3, v36

    invoke-direct {v1, v3}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->T:Ljava/lang/String;

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MainActivity;->K:Z

    if-nez v1, :cond_20

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v3, v35

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_12

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v34

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    :goto_5
    move-object/from16 v4, v31

    goto/16 :goto_9

    :cond_12
    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v3, v33

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_13

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, p1

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto :goto_5

    :cond_13
    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v3, v18

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_1b

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v3, v32

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_14

    :goto_6
    move-object/from16 v4, v31

    goto/16 :goto_8

    :cond_14
    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v4, v31

    invoke-virtual {v1, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_17

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v48

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    iget-boolean v1, v0, Lcom/tatkal/train/quick/MainActivity;->k0:Z

    if-eqz v1, :cond_15

    const-string v1, "PAYTM UPI"

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    :cond_15
    sget-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    const-string v2, "PhonePe UPI"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_16

    move-object/from16 v1, v30

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    :cond_16
    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v49

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->l0:Ljava/lang/String;

    goto/16 :goto_9

    :cond_17
    move-object/from16 v1, v30

    iget-object v3, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v5, v19

    invoke-virtual {v3, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_18

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v50

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto/16 :goto_9

    :cond_18
    iget-object v3, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v5, v29

    invoke-virtual {v3, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_1a

    iget-object v3, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    move-object/from16 v5, v28

    invoke-virtual {v3, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_19

    goto :goto_7

    :cond_19
    iget-object v3, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    invoke-virtual {v3, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1d

    new-instance v1, Ljava/lang/String;

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto :goto_9

    :cond_1a
    :goto_7
    const-string v1, "IRCTC"

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto :goto_9

    :cond_1b
    move-object/from16 v3, v32

    goto :goto_6

    :goto_8
    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v27

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->L:Ljava/lang/String;

    invoke-virtual {v1, v3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1d

    sget-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    const-string v2, "American Express"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1c

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v37

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->b0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v38

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->c0:Ljava/lang/String;

    goto :goto_9

    :cond_1c
    sget-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    const-string v2, "International cards (Powered by ATOM)"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1d

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v39

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->b0:Ljava/lang/String;

    new-instance v1, Ljava/lang/String;

    move-object/from16 v2, v40

    invoke-direct {v1, v2}, Ljava/lang/String;-><init>([B)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->c0:Ljava/lang/String;

    :cond_1d
    :goto_9
    sget-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    const-string v2, "IRCTC iPay UPI"

    if-eqz v1, :cond_1e

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_1e

    move-object/from16 v3, v21

    move-object/from16 v1, v25

    invoke-virtual {v3, v4, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_a

    :cond_1e
    move-object/from16 v3, v21

    const-string v1, "BHIM/ UPI/ USSD"

    invoke-virtual {v3, v4, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :goto_a
    sget-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    if-eqz v1, :cond_20

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_20

    const-string v1, "IRCTC iPay"

    sput-object v1, Lcom/tatkal/train/quick/MainActivity;->p1:Ljava/lang/String;

    goto :goto_b

    :cond_1f
    move-object/from16 v20, v13

    move-object/from16 v26, v15

    :cond_20
    :goto_b
    invoke-interface {v7}, Landroid/database/Cursor;->close()V

    invoke-virtual/range {v24 .. v24}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual/range {v23 .. v23}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    const/4 v2, 0x6

    new-array v1, v2, [Ldo1;

    new-instance v2, Lz3;

    const/4 v4, 0x5

    invoke-direct {v2, v0, v4}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from PASSENGER_INFO where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    move-object/from16 v6, v20

    const/4 v15, 0x0

    invoke-static {v4, v5, v6, v3, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    const/4 v5, 0x0

    :goto_c
    :try_start_3
    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v7

    if-eqz v7, :cond_22

    new-instance v7, Ldo1;

    invoke-direct {v7}, Ljava/lang/Object;-><init>()V

    aput-object v7, v1, v5

    const/4 v8, 0x1

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Ldo1;->j:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v8, 0x2

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Ldo1;->g:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v8, 0x3

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Ldo1;->i:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v8, 0x4

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Ldo1;->l:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v8, 0x5

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Ldo1;->m:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v9, 0x6

    invoke-interface {v4, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v10

    iput-object v10, v7, Ldo1;->n:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v9, 0x7

    invoke-interface {v4, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v10

    iput-object v10, v7, Ldo1;->a:Ljava/lang/String;

    aget-object v7, v1, v5

    const/16 v10, 0x8

    invoke-interface {v4, v10}, Landroid/database/Cursor;->getInt(I)I

    move-result v11

    iput v11, v7, Ldo1;->b:I

    aget-object v7, v1, v5

    const/16 v10, 0x9

    invoke-interface {v4, v10}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v11

    iput-object v11, v7, Ldo1;->c:Ljava/lang/String;

    aget-object v7, v1, v5

    const/16 v11, 0xa

    invoke-interface {v4, v11}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    iput-object v12, v7, Ldo1;->d:Ljava/lang/String;

    const/16 v7, 0xb

    invoke-interface {v4, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v12

    const-string v13, "NA"

    invoke-virtual {v12, v13}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v12

    if-nez v12, :cond_21

    aget-object v12, v1, v5

    invoke-interface {v4, v7}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v13

    iput-object v13, v12, Ldo1;->f:Ljava/lang/String;

    goto :goto_d

    :cond_21
    aget-object v12, v1, v5

    const-string v13, "V"

    iput-object v13, v12, Ldo1;->f:Ljava/lang/String;

    :goto_d
    aget-object v12, v1, v5

    const/16 v13, 0xc

    invoke-interface {v4, v13}, Landroid/database/Cursor;->getInt(I)I

    move-result v14

    iput v14, v12, Ldo1;->e:I

    aget-object v12, v1, v5

    iget v14, v12, Ldo1;->b:I

    const/16 v14, 0xd

    invoke-interface {v4, v14}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v15

    invoke-static {v15}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v15

    iput-boolean v15, v12, Ldo1;->h:Z

    aget-object v12, v1, v5

    const/16 v15, 0xe

    invoke-interface {v4, v15}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v7

    iput-object v7, v12, Ldo1;->k:Ljava/lang/String;

    aget-object v7, v1, v5

    const/16 v12, 0xf

    invoke-interface {v4, v12}, Landroid/database/Cursor;->getInt(I)I

    move-result v8

    iput v8, v7, Ldo1;->o:I

    add-int/lit8 v5, v5, 0x1

    iget v7, v0, Lcom/tatkal/train/quick/MainActivity;->t0:I

    const/16 v17, 0x1

    add-int/lit8 v7, v7, 0x1

    iput v7, v0, Lcom/tatkal/train/quick/MainActivity;->t0:I
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_3

    goto/16 :goto_c

    :catch_3
    :cond_22
    invoke-interface {v4}, Landroid/database/Cursor;->close()V

    invoke-virtual {v3}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    iget v2, v0, Lcom/tatkal/train/quick/MainActivity;->t0:I

    new-array v2, v2, [Ldo1;

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->z:[Ldo1;

    const/4 v4, 0x0

    :goto_e
    iget v2, v0, Lcom/tatkal/train/quick/MainActivity;->t0:I

    if-ge v4, v2, :cond_23

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->z:[Ldo1;

    aget-object v3, v1, v4

    aput-object v3, v2, v4

    add-int/lit8 v4, v4, 0x1

    goto :goto_e

    :cond_23
    const/4 v4, 0x2

    new-array v1, v4, [Lrl;

    new-instance v2, Lz3;

    const/4 v8, 0x1

    invoke-direct {v2, v0, v8}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from CHILD_INFO where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static {v4, v5, v6, v3, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    const/4 v5, 0x0

    :goto_f
    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v7

    if-eqz v7, :cond_24

    new-instance v7, Lrl;

    invoke-direct {v7}, Ljava/lang/Object;-><init>()V

    aput-object v7, v1, v5

    const/4 v8, 0x1

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v9

    iput-object v9, v7, Lrl;->a:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v9, 0x2

    invoke-interface {v4, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v10

    iput-object v10, v7, Lrl;->b:Ljava/lang/String;

    aget-object v7, v1, v5

    const/4 v9, 0x3

    invoke-interface {v4, v9}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v10

    iput-object v10, v7, Lrl;->c:Ljava/lang/String;

    add-int/2addr v5, v8

    iget v7, v0, Lcom/tatkal/train/quick/MainActivity;->u0:I

    add-int/2addr v7, v8

    iput v7, v0, Lcom/tatkal/train/quick/MainActivity;->u0:I

    goto :goto_f

    :cond_24
    invoke-interface {v4}, Landroid/database/Cursor;->close()V

    invoke-virtual {v3}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    iget v2, v0, Lcom/tatkal/train/quick/MainActivity;->u0:I

    new-array v2, v2, [Lrl;

    iput-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->A:[Lrl;

    const/4 v4, 0x0

    :goto_10
    iget v2, v0, Lcom/tatkal/train/quick/MainActivity;->u0:I

    if-ge v4, v2, :cond_25

    iget-object v2, v0, Lcom/tatkal/train/quick/MainActivity;->A:[Lrl;

    aget-object v3, v1, v4

    aput-object v3, v2, v4

    add-int/lit8 v4, v4, 0x1

    goto :goto_10

    :cond_25
    new-instance v1, Lz3;

    const/4 v2, 0x6

    invoke-direct {v1, v0, v2}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v2

    new-instance v3, Ljava/lang/StringBuilder;

    const-string v4, "select * from POD_INFO where FORM_NAME = \'"

    invoke-direct {v3, v4}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static {v3, v4, v6, v2, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v3

    invoke-interface {v3}, Landroid/database/Cursor;->moveToNext()Z

    move-result v4

    if-eqz v4, :cond_26

    const/4 v8, 0x1

    invoke-interface {v3, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v4

    iput-object v4, v0, Lcom/tatkal/train/quick/MainActivity;->n0:Ljava/lang/String;

    :cond_26
    invoke-interface {v3}, Landroid/database/Cursor;->close()V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    new-instance v1, Lz3;

    const/4 v2, 0x4

    invoke-direct {v1, v0, v2}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v2

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from INSURANCE where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static {v4, v5, v6, v2, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v5

    if-eqz v5, :cond_27

    const/4 v8, 0x1

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getInt(I)I

    move-result v5

    iput v5, v0, Lcom/tatkal/train/quick/MainActivity;->E:I

    :cond_27
    invoke-interface {v4}, Landroid/database/Cursor;->close()V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    new-instance v1, Lz3;

    const/16 v2, 0x8

    invoke-direct {v1, v0, v2}, Lz3;-><init>(Landroid/content/Context;I)V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->getReadableDatabase()Landroid/database/sqlite/SQLiteDatabase;

    move-result-object v2

    new-instance v4, Ljava/lang/StringBuilder;

    const-string v5, "select * from SEC_ANS where FORM_NAME = \'"

    invoke-direct {v4, v5}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v5, v0, Lcom/tatkal/train/quick/MainActivity;->H0:Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static {v4, v5, v6, v2, v15}, Lq90;->e(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v4

    invoke-interface {v4}, Landroid/database/Cursor;->moveToNext()Z

    move-result v5

    if-eqz v5, :cond_28

    const/4 v8, 0x1

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v8, 0x2

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v8, 0x3

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    const/4 v8, 0x4

    invoke-interface {v4, v8}, Landroid/database/Cursor;->getInt(I)I

    :cond_28
    invoke-interface {v3}, Landroid/database/Cursor;->close()V

    invoke-virtual {v2}, Landroid/database/sqlite/SQLiteClosable;->close()V

    invoke-virtual {v1}, Landroid/database/sqlite/SQLiteOpenHelper;->close()V

    sget v1, Lhw1;->progressBar3:I

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/ProgressBar;

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->r0:Landroid/widget/ProgressBar;

    sget v1, Lhw1;->continueBtn:I

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/Button;

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->e1:Landroid/widget/Button;

    new-instance v1, Landroid/app/ProgressDialog;

    invoke-direct {v1, v0}, Landroid/app/ProgressDialog;-><init>(Landroid/content/Context;)V

    const/4 v4, 0x0

    invoke-virtual {v1, v4}, Landroid/app/Dialog;->setCancelable(Z)V

    new-instance v1, Landroid/app/ProgressDialog;

    invoke-direct {v1, v0}, Landroid/app/ProgressDialog;-><init>(Landroid/content/Context;)V

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->g1:Landroid/app/ProgressDialog;

    invoke-virtual {v1, v4}, Landroid/app/Dialog;->setCancelable(Z)V

    invoke-static {v4}, Landroid/webkit/WebView;->setWebContentsDebuggingEnabled(Z)V

    sget v1, Lhw1;->webView:I

    invoke-virtual {v0, v1}, Landroidx/appcompat/app/AppCompatActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Lcom/tatkal/train/quick/AdvancedWebView;

    iput-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const/4 v8, 0x1

    invoke-virtual {v1, v8}, Landroid/webkit/WebView;->clearCache(Z)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1}, Landroid/webkit/WebView;->clearHistory()V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1}, Landroid/webkit/WebView;->clearFormData()V

    invoke-static {}, Landroid/webkit/WebStorage;->getInstance()Landroid/webkit/WebStorage;

    move-result-object v1

    invoke-virtual {v1}, Landroid/webkit/WebStorage;->deleteAllData()V

    invoke-static {}, Landroid/webkit/CookieManager;->getInstance()Landroid/webkit/CookieManager;

    move-result-object v1

    const/4 v15, 0x0

    invoke-virtual {v1, v15}, Landroid/webkit/CookieManager;->removeAllCookies(Landroid/webkit/ValueCallback;)V

    invoke-virtual {v1}, Landroid/webkit/CookieManager;->flush()V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1, v0, v0}, Lcom/tatkal/train/quick/AdvancedWebView;->setListener(Landroid/app/Activity;Ld4;)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const/4 v8, 0x1

    invoke-virtual {v1, v8}, Lcom/tatkal/train/quick/AdvancedWebView;->setGeolocationEnabled(Z)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1, v8}, Lcom/tatkal/train/quick/AdvancedWebView;->setMixedContentAllowed(Z)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1, v8}, Lcom/tatkal/train/quick/AdvancedWebView;->setCookiesEnabled(Z)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v1, v8}, Lcom/tatkal/train/quick/AdvancedWebView;->setThirdPartyCookiesEnabled(Z)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance v2, Laa1;

    invoke-direct {v2, v0}, Laa1;-><init>(Lcom/tatkal/train/quick/MainActivity;)V

    const-string v3, "Step"

    invoke-virtual {v1, v2, v3}, Landroid/webkit/WebView;->addJavascriptInterface(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v1, "X-Requested-With"

    const-string v2, "com.android.chrome"

    iget-object v3, v0, Lcom/tatkal/train/quick/MainActivity;->f1:Ljava/util/HashMap;

    invoke-virtual {v3, v1, v2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->e1:Landroid/widget/Button;

    new-instance v2, Lxf;

    const/4 v4, 0x6

    invoke-direct {v2, v0, v4}, Lxf;-><init>(Ljava/lang/Object;I)V

    invoke-virtual {v1, v2}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance v2, Lb4;

    const/4 v4, 0x2

    invoke-direct {v2, v0, v4}, Lb4;-><init>(Landroid/view/KeyEvent$Callback;I)V

    invoke-virtual {v1, v2}, Lcom/tatkal/train/quick/AdvancedWebView;->setWebChromeClient(Landroid/webkit/WebChromeClient;)V

    sget-object v1, Lw3;->a:Ljava/util/List;

    invoke-virtual {v0}, Landroidx/appcompat/app/AppCompatActivity;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    sget v2, Lev1;->blocked_domains:I

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getStringArray(I)[Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object v1

    sput-object v1, Lw3;->a:Ljava/util/List;

    iget-object v1, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    new-instance v2, La4;

    invoke-direct {v2, v0}, La4;-><init>(Lcom/tatkal/train/quick/MainActivity;)V

    invoke-virtual {v1, v2}, Lcom/tatkal/train/quick/AdvancedWebView;->setWebViewClient(Landroid/webkit/WebViewClient;)V

    invoke-static {}, Landroid/webkit/CookieManager;->getInstance()Landroid/webkit/CookieManager;

    move-result-object v1

    const/4 v15, 0x0

    invoke-virtual {v1, v15}, Landroid/webkit/CookieManager;->removeAllCookies(Landroid/webkit/ValueCallback;)V

    new-instance v1, Ljava/util/HashMap;

    invoke-direct {v1}, Ljava/util/HashMap;-><init>()V

    const-string v2, "Sec-Ch-Ua"

    const-string v3, "\"Not;A=Brand\";v=\"99\", \"Google Chrome\";v=\"139\", \"Chromium\";v=\"139\""

    invoke-virtual {v1, v2, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "Sec-Ch-Ua-Mobile"

    const-string v3, "?1"

    invoke-virtual {v1, v2, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "Sec-Ch-Ua-Platform"

    const-string v3, "\"Android\""

    invoke-virtual {v1, v2, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "Upgrade-Insecure-Requests"

    move-object/from16 v3, v26

    invoke-virtual {v1, v2, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const-string v2, "User-agent"

    const-string v3, "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36"

    invoke-virtual {v1, v2, v3}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    iget-object v0, v0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const-string v2, "https://www.irctc.co.in/nget/train-search"

    invoke-virtual {v0, v2, v1}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;Ljava/util/Map;)V

    return-void
.end method

.method public final onCreateOptionsMenu(Landroid/view/Menu;)Z
    .locals 1

    invoke-virtual {p0}, Landroidx/appcompat/app/AppCompatActivity;->getMenuInflater()Landroid/view/MenuInflater;

    move-result-object p0

    sget v0, Ltw1;->booking_menu:I

    invoke-virtual {p0, v0, p1}, Landroid/view/MenuInflater;->inflate(ILandroid/view/Menu;)V

    const/4 p0, 0x1

    return p0
.end method

.method public final onDestroy()V
    .locals 2

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    :try_start_0
    invoke-virtual {v0}, Landroid/view/View;->getParent()Landroid/view/ViewParent;

    move-result-object v1

    check-cast v1, Landroid/view/ViewGroup;

    invoke-virtual {v1, v0}, Landroid/view/ViewGroup;->removeView(Landroid/view/View;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    :try_start_1
    invoke-virtual {v0}, Landroid/view/ViewGroup;->removeAllViews()V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    :catch_1
    invoke-virtual {v0}, Landroid/webkit/WebView;->destroy()V

    invoke-super {p0}, Landroidx/appcompat/app/AppCompatActivity;->onDestroy()V

    const/4 v0, 0x0

    sput v0, Lcom/tatkal/train/quick/MainActivity;->q1:I

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->Y0:Lcom/tatkal/train/quick/OTPBroadcastReceiver;

    if-eqz v0, :cond_0

    invoke-virtual {p0, v0}, Landroid/content/Context;->unregisterReceiver(Landroid/content/BroadcastReceiver;)V

    :cond_0
    return-void
.end method

.method public final onOptionsItemSelected(Landroid/view/MenuItem;)Z
    .locals 2

    invoke-interface {p1}, Landroid/view/MenuItem;->getItemId()I

    move-result p1

    sget v0, Lhw1;->show_password:I

    if-ne p1, v0, :cond_0

    invoke-virtual {p0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object p1

    invoke-virtual {p0}, Landroid/app/Activity;->finish()V

    invoke-virtual {p0, p1}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    goto :goto_1

    :cond_0
    sget v0, Lhw1;->save_html:I

    if-ne p1, v0, :cond_1

    new-instance p1, Landroid/content/Intent;

    const-string v0, "android.intent.action.SEND"

    invoke-direct {p1, v0}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    const-string v0, "message/rfc822"

    invoke-virtual {p1, v0}, Landroid/content/Intent;->setType(Ljava/lang/String;)Landroid/content/Intent;

    const-string v0, "support@afrestudios.com"

    filled-new-array {v0}, [Ljava/lang/String;

    move-result-object v0

    const-string v1, "android.intent.extra.EMAIL"

    invoke-virtual {p1, v1, v0}, Landroid/content/Intent;->putExtra(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/Intent;

    const-string v0, "android.intent.extra.TEXT"

    iget-object v1, p0, Lcom/tatkal/train/quick/MainActivity;->Z0:Ljava/lang/String;

    invoke-virtual {p1, v0, v1}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    const-string v0, "android.intent.extra.SUBJECT"

    const-string v1, "Bug Report"

    invoke-virtual {p1, v0, v1}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    :try_start_0
    const-string v0, "Send mail"

    invoke-static {p1, v0}, Landroid/content/Intent;->createChooser(Landroid/content/Intent;Ljava/lang/CharSequence;)Landroid/content/Intent;

    move-result-object p1

    invoke-virtual {p0, p1}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V
    :try_end_0
    .catch Landroid/content/ActivityNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    const-string p1, "There are no email apps installed."

    const/4 v0, 0x0

    invoke-static {p0, p1, v0}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object p1

    invoke-virtual {p1}, Landroid/widget/Toast;->show()V

    :goto_0
    iget-object p0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    const-string p1, "javascript:function guruHoJaShuru() {Step.copyHTML(document.getElementsByTagName(\'html\')[0].outerHTML);}\nguruHoJaShuru()"

    invoke-virtual {p0, p1}, Lcom/tatkal/train/quick/AdvancedWebView;->loadUrl(Ljava/lang/String;)V

    :cond_1
    :goto_1
    const/4 p0, 0x1

    return p0
.end method

.method public final onPause()V
    .locals 1

    iget-object v0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {v0}, Lcom/tatkal/train/quick/AdvancedWebView;->onPause()V

    invoke-super {p0}, Landroidx/fragment/app/FragmentActivity;->onPause()V

    return-void
.end method

.method public final onResume()V
    .locals 0

    invoke-super {p0}, Landroidx/fragment/app/FragmentActivity;->onResume()V

    iget-object p0, p0, Lcom/tatkal/train/quick/MainActivity;->s0:Lcom/tatkal/train/quick/AdvancedWebView;

    invoke-virtual {p0}, Lcom/tatkal/train/quick/AdvancedWebView;->onResume()V

    return-void
.end method
