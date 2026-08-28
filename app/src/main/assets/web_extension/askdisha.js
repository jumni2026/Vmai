
var isTyping = true;
var usingKeyboard = false;
var loadingComplete = false;
var pymtChoice = '';
var pymtChoiceLong = '';
var bankChoice = '';
var waitForPTResponse = 0;
var verificationNeeded = false;
var welcomeAnimShown = false;

const executedFlags = {
    login: false,
    journey: false,
    train: false,
    verifyUserID: false,
    passenger: false,
    reviewOTP: false,
    payment: false,
    booked: false,
};

const port = browser.runtime.connect({
    name: "page"
});

browser.runtime.onMessage.addListener((msg, sender) => {
    console.log('onMessage action in js: ' + msg.action + ' | ' + window.location.href);
    if (msg.action === "ACCEPT_FARE") {
        console.log('Fare accepted');
        waitForPTResponse = 2;
        (async () => {
            await acceptFareAndContinue();
        })();
    } else if (msg.action === "DECLINE_FARE") {
        waitForPTResponse = 3;
    } else if (msg.action === "SAVE_HTML") {
        var htmlString = document.getElementsByTagName('html')[0].outerHTML;

        var data = {
            action: 'DOWNLOAD_HTML',
            html: htmlString
        };

        // convert to JSON string
        let jsonString = JSON.stringify(data);
        browser.runtime.sendNativeMessage("browser", jsonString);
    } else if (msg.action === "OTP_RECEIVED" && bookingObj.allow_otp_autofill) {
        var otp = msg.otp;
        if (document.getElementsByClassName('input')[0] && document.getElementsByClassName('input')[0].innerHTML.indexOf('OTP') > 0) {
            console.log('OTP Received');
            autofillLoginOTP(otp);
        } else if(document.getElementsByClassName('inputSchemes')[0]) {
            autofillBookingOTP(otp);
        } else if(document.getElementsByClassName('credit-drawer')[0] && document.getElementsByClassName('credit-drawer')[0].getElementsByTagName('button')[0]) {
            console.log('Card OTP Received');
            autofillCardOTP(otp);
        }
    }
});

async function autofillLoginOTP(otp) {
    await sendRealKeys(document.getElementsByClassName('input')[0].getElementsByTagName('input')[0], otp,
            {delay: false, tap:true});
}

async function autofillBookingOTP(otp) {
    await sendRealKeys(document.getElementsByClassName('inputSchemes')[0], otp, {delay: false, tap:true});
    await delay(50);
    document.getElementsByClassName('steps')[0].getElementsByTagName('button')[0].click();
}

async function autofillCardOTP(otp) {
    console.log('Card OTP: ' + otp);
    const shortInputs = document.querySelectorAll('input[maxlength="1"]');
    var index = 0;

    if(document.activeElement != shortInputs[0]) {
        await sendTap(shortInputs[0]);
        await delay(500);
        if(document.activeElement != shortInputs[0]) {
            shortInputs[0].focus();
        }
    }

    var activeElement = shortInputs[0];
    await sendOTPKeys(shortInputs[0], otp.charAt(0) + '', {delay: false, ms: 200});

    console.log('Card OTP first digit complete');

    while(shortInputs[5].value == '') {
        if(document.activeElement != activeElement) {
            activeElement = document.activeElement;
            index++;
            await sendOTPKeys(shortInputs[index], otp.charAt(index) + '', {delay: false, ms: 200});
        }
        await delay(10);
    }
    await delay(50);
    document.getElementsByClassName('credit-drawer')[0].getElementsByTagName('button')[0].click();
}

function openUpiApp() {
    console.log('Initiate open UPI app');
    let data = {
      action: 'OPEN_UPI'
    };

    // convert to JSON string
    let jsonString = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jsonString);
}

async function acceptFareAndContinue() {
    // 1. Select the correct payment choice
    console.log('Inside accept fare');
    autofillPaymentSelection();
}

function isElementVisible(el) {
  if (!el) return false;

  const rect = el.getBoundingClientRect();

  // Calculate the element's center point
  const centerX = rect.left + rect.width / 2;
  const centerY = rect.top + rect.height / 2;

  // Check if the center point lies within the viewport
  return (
    centerX >= 0 &&
    centerX <= window.innerWidth &&
    centerY >= 0 &&
    centerY <= window.innerHeight
  );
}

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function sendOTPKeys(elem, value, opts = {}) {
    console.log('Inside sendOTPKeys function: ' + value);

    var data = {
        action: 'SEND_REAL_KEYS',
        text: value,
        delay: opts.delay,
        ms: opts.ms,
        actual: 'NA'
    };

    // convert to JSON string
    var jss = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jss);
}

async function sendTap(el, opts = {}) {
    console.log('Sending tap to ' + el.outerHTML);

    if(!isElementVisible(el)) {
        el.scrollIntoView();
        await delay(100);
    }

    var r = el.getBoundingClientRect();
    var x = (r.left + r.width / 2) * window.devicePixelRatio;
    var y = (r.top + r.height / 2) * window.devicePixelRatio;

    if (opts.edge) {
        x = (r.left + r.width / 2 - opts.x) * window.devicePixelRatio;
        y = (r.top + r.height / 2 - opts.y) * window.devicePixelRatio;
    }

    var data = {
        action: 'SEND_TAP',
        x: x,
        y: y
    };

    // convert to JSON string
    var jss = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jss);
}

async function clearText(el) {
    var data = {
            action: 'CLEAR_TEXT'
        };

    // convert to JSON string
    var jss = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jss);

    var cnt = 0;
    while(true) {
        if(el.value == '') {
            console.log('Value cleared');
            break;
        } else {
            await delay(100);
        }
        cnt++;

        if(cnt>= 20) {
            break;
        }
    }
}

async function sendRemRealKeys(elem, value, remValue, opts = {}) {
    console.log('Inside sendRemRealKeys function: ' + value);

    if(!document.contains(elem)) {
        return;
    }
    isTyping = true;

    if(!isElementVisible(elem)) {
        elem.scrollIntoView();
        await delay(100);
    }

    console.log('Requesting element focus');
    if(opts.isStn) {
        await sendTap(elem);
        await delay(500);
    }

    if (document.activeElement != elem) {
        if(opts.tap !== false) {
            console.log('Tap required');
            await sendTap(elem);
        } else {
            console.log('Focus required');
            elem.focus();
        }
    } else {
        console.log('Element is active');
    }

    while (true) {
        if (document.activeElement == elem) {
            break;
        } else {
            await sendTap(elem);
            await delay(500);
        }
    }

    var ms = 100;
    if(opts.ms) {
        ms = opts.ms;
    }

    var isDelay = false;
    var waitTime = 2000;
    if(opts.delay === true) {
        isDelay = true;
        waitTime = (remValue.length) * (ms + 50) + 500;
    }

    var data = {
        action: 'SEND_REAL_KEYS',
        text: value,
        delay: isDelay,
        ms: opts.ms
    };

    // convert to JSON string
    var jss = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jss);

    await delay(500);
    waitTime-= 500;

    while (true) {
        if (elem.value == value) {
            console.log('Value filled correctly');
            break;
        } else {
            if(waitTime < 0 || !isTyping) {
                isTyping = true;
                if(value.indexOf(elem.value) == 0) {
                    await sendRemRealKeys(elem, value, value.substring(elem.value.length), opts);
                } else {
                    console.log('Incorrect value filled: ' + elem.value);
                    if(!usingKeyboard) {
                        elem.value = '';
                    } else {
                        await clearText(elem);
                    }
                    await sendRealKeys(elem, value, opts);
                }
            } else {
                await delay(100);
                waitTime-= 100;
            }
        }
    }
}

async function sendRealKeys(elem, value, opts = {}) {
    console.log('Inside sendRealKeys function: ' + value);
    if(elem.value != '') {
        elem.value = '';
    }

    if(!document.contains(elem)) {
        return;
    }
    isTyping = true;

    if(!isElementVisible(elem)) {
        elem.scrollIntoView();
        await delay(100);
    }

    console.log('Requesting element focus');

    var fixedDelay = 50;
    var bound = 100;
    if(value != '#FROM_STN' && value != '#TO_STN') {
        fixedDelay = 550;
        bound = 301;
    }

    await sendTap(elem);
    while (document.activeElement != elem) {
        if(opts.tap !== false) {
            console.log('Tap required');
            await sendTap(elem);
        } else {
            console.log('Focus required');
            elem.focus();
        }
        await delay(200);
    }

    var r = Math.floor(Math.random() * bound);

    while (true) {
        if (document.activeElement == elem) {
            console.log(elem.outerHTML + ' has focus');
            break;
        } else {
            console.log('No focus');
            await sendTap(elem);
            await delay(500);
        }
    }

    var ms = 100;

    if(opts.ms) {
        ms = opts.ms;
    }

    var actual = 'null';
    if(opts.actual) {
        actual = opts.actual;
    }

    console.log('Start filling');
    var isDelay = false;
    var waitTime = 750;
    if(opts.delay === true) {
        isDelay = true;
        waitTime = 1500;
    }

    console.log('Calling java sendRealKeys');

    var data = {
        action: 'SEND_REAL_KEYS',
        text: value,
        delay: isDelay,
        ms: opts.ms,
        actual: actual,
        card: (opts.card == true)
    };

    // convert to JSON string
    var jss = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jss);

    console.log('Called java sendRealKeys');

    //await delay(200);
    waitTime-= 200;

    console.log('start while loop');

    while (true) {
        console.log('inside while loop');
        if(!document.contains(elem)) {
            console.log('Element not present. Returning');
            return;
        }
        var isValid = false;
        if(opts.card) {
            if(elem.value.replaceAll(' ', '') == value) {
                isValid = true;
            }
        }
        if(opts.expiry) {
            if(elem.value.replaceAll('/', '') == value) {
                isValid = true;
            }
        }

        if (elem.value == value || isValid) {
            console.log('Value filled correctly');
            break;
        } else {
            console.log('Value filled incorrectly');
            if(waitTime < 0 || !isTyping) {
                console.log('sendRealKeys var check: ' + waitTime + ', typing: ' + isTyping);
                isTyping = true;
                if(value.indexOf(elem.value) == -2) { // 0
                    await sendRemRealKeys(elem, value, value.substring(elem.value.length), opts);
                } else {
                    console.log('Incorrect value filled: ' + elem.value);

                    if(!usingKeyboard) {
                        elem.value = '';
                    } else {
                        await clearText(elem);
                    }

                    await sendRealKeys(elem, value, opts);
                    console.log('Called sendrealkeys again');
                    break;
                }
            } else {
                await delay(100);
                waitTime-= 100;
            }
        }
    }
}

function print(message) {
    var data = {
        action: 'PRINT',
        msg: message
    };

    // convert to JSON string
    let jsonString = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jsonString);
}

function printAvl(avl) {
    var data = {
        action: 'PRINT_AVL',
        avl: avl
    };

    // convert to JSON string
    let jsonString = JSON.stringify(data);
    browser.runtime.sendNativeMessage("browser", jsonString);
}

function showFareAlert(amount) {
    if (waitForPTResponse == 0) {
        waitForPTResponse = 1;

        var data = {
            action: 'FARE_ALERT',
            amount: amount
        };

        // convert to JSON string
        let jsonString = JSON.stringify(data);
        browser.runtime.sendNativeMessage("browser", jsonString);
    }
}

async function autofillLogin() {
    console.log('askdisha.js sign in click');

    showWelcomeAnim();
    await delay(4000);
    welcomeAnimShown = true;

    document.getElementsByClassName('sign-in')[0].click();

    while(!document.getElementById('drawer-footer')) {
        await delay(50);
    }

    await sendRealKeys(document.getElementsByClassName('input')[0].getElementsByTagName('input')[0], bookingObj.disha_mobile,
        {delay: true, tap:true});
    document.getElementById('drawer-footer').getElementsByTagName('button')[0].click();
}

function clickCalendarDay(dateStr, opts = {}) {
    const { monthSelector = ".month-title", gridSelector = "ul, ol" } = opts;

    const [dd, MM] = dateStr.split("-");   // "dd-MM-yyyy"
    const day = parseInt(dd, 10);
    const monthIndex = parseInt(MM, 10) - 1;

    // Month index → English month name (August, September, ...)
    const monthName = new Date(2000, monthIndex, 1).toLocaleString("en-US", { month: "long" });

    // 1) Find the month title node that matches the target month
    const monthTitles = document.querySelectorAll(monthSelector);
    let monthTitleEl = null;
    for (const el of monthTitles) {
      if (el.textContent.trim().toLowerCase() === monthName.toLowerCase()) {
        monthTitleEl = el;
        break;
      }
    }
    if (!monthTitleEl) {
      console.warn("Month not found:", monthName);
      return false;
    }

    // 2) Locate the days grid near this month title
    //    (calendar UIs often wrap both under the same parent)
    const container = monthTitleEl.closest("div, section, article") || monthTitleEl.parentElement;
    if (!container) {
      console.warn("Month container not found for:", monthName);
      return false;
    }

    // Prefer the first list/grid after the title within the same container
    // Tweak selector if you know the exact class, e.g., "ul.day-grid"
    const grids = container.querySelectorAll(gridSelector);
    let daysGrid = null;
    for (const g of grids) {
      // Heuristic: grid that contains li > span with numbers 1..31
      if (g.querySelector("li span")) {
        daysGrid = g;
        break;
      }
    }
    if (!daysGrid) {
      console.warn("Days grid not found for:", monthName);
      return false;
    }

    // 3) Find the span whose text equals the target day and click it
    const spans = daysGrid.querySelectorAll("li span");
    for (const span of spans) {
      const n = parseInt(span.textContent.trim(), 10);
      if (n === day) {
        span.click(); // the click handler is on the <span>
        return true;
      }
    }

    console.warn(`Day ${day} not found in month ${monthName}`);
    return false;
}

function areDatesEqual(dateStr1, dateStr2) {
  // 1. Create a lookup dictionary for the month abbreviations
  const monthMap = {
    Jan: '01', Feb: '02', Mar: '03', Apr: '04', May: '05', Jun: '06',
    Jul: '07', Aug: '08', Sep: '09', Oct: '10', Nov: '11', Dec: '12'
  };

  // 2. Clean up and split "21 Apr, 2026" into parts
  // replace(',', '') removes the comma, then we split by space
  const parts = dateStr1.replace(',', '').split(' ');

  if (parts.length !== 3) return false; // Safety check

  // 3. Extract the parts (padding the day with a '0' just in case it's "5" instead of "05")
  const day = parts[0].padStart(2, '0');
  const month = monthMap[parts[1]];
  const year = parts[2];

  // 4. Reconstruct it into the "dd-MM-yyyy" format
  const convertedDateStr = `${day}-${month}-${year}`;

  // 5. Compare the two strings
  return convertedDateStr === dateStr2;
}

async function showWelcomeAnim() {
    const img = document.createElement('img');
    img.src = browser.runtime.getURL('welcome.gif');
    Object.assign(img.style, {
      position: 'fixed',
      top: '50%',
      left: '50%',
      transform: 'translate(-50%, -50%)',
      zIndex: '99999',
      width: `${window.innerWidth - 100}px`,
      height: 'auto',
      maxHeight: '90vh'
    });
    document.body.appendChild(img);

    // remove after ~3 seconds (adjust as per gif duration)
    setTimeout(() => img.remove(), 4000);
}

async function autofillJourney() {
    console.log('askdisha.js start journey autofill');
    if(!welcomeAnimShown) {
        welcomeAnimShown = true;
        showWelcomeAnim();
        await delay(4000);
    }

    // stations autofill
    var currentFromCode = document.querySelector('[aria-label="From"]').innerText.split(' - ')[1];
    var currentToCode = document.querySelector('[aria-label="To"]').innerText.split(' - ')[1];

    var fromCode = bookingObj.from_stn.split(' - ')[1].trim();
    var toCode = bookingObj.to_stn.split(' - ')[1].trim();

    if(currentFromCode == fromCode && currentToCode == toCode) {
        // do nothing
    } else {
        if(currentToCode == fromCode) {
            document.querySelector('[aria-label="From"]').parentNode.children[1].children[0].click();

            if(currentFromCode == toCode) {
                // do nothing
            } else {
                document.querySelector('[aria-label="From"]').click();
                await delay(1000);
                if(document.getElementById('station-textbox').value != '') {
                    document.getElementById('station-textbox').value = '';
                }
                await sendRealKeys(document.getElementById('station-textbox'), fromCode, {delay: true, tap:true, isStn: true});

                if(!document.getElementsByClassName('station-item')[0]) {
                    while(!document.getElementsByClassName('station-item')[0]) {
                        await delay(50);
                    }
                }
                while(document.getElementsByClassName('station-item')[0].getElementsByTagName('p')[0].innerText != fromCode) {
                    await delay(50);
                }
                document.getElementsByClassName('station-item')[0].getElementsByTagName('div')[0].click();
                await delay(1000);
            }
        } else {
            if(currentFromCode != fromCode) {
                document.querySelector('[aria-label="From"]').click();
                await delay(1000);
                console.log('askdisha.js clicked from: ' + fromCode);

                if(document.getElementById('station-textbox').value != '') {
                    document.getElementById('station-textbox').value = '';
                }
                await sendRealKeys(document.getElementById('station-textbox'), fromCode, {delay: true, tap:true, isStn: true});

                if(!document.getElementsByClassName('station-item')[0]) {
                    while(!document.getElementsByClassName('station-item')[0]) {
                        await delay(50);
                    }
                }
                while(document.getElementsByClassName('station-item')[0].getElementsByTagName('p')[0].innerText != fromCode) {
                    await delay(50);
                }
                document.getElementsByClassName('station-item')[0].getElementsByTagName('div')[0].click();
                await delay(1000);
            }

            if(currentToCode != toCode) {
                document.querySelector('[aria-label="To"]').click();
                await delay(1000);
                if(document.getElementById('station-textbox').value != '') {
                    document.getElementById('station-textbox').value = '';
                }
                await sendRealKeys(document.getElementById('station-textbox'), toCode, {delay: true, tap:true, isStn: true});

                if(!document.getElementsByClassName('station-item')[0]) {
                    while(!document.getElementsByClassName('station-item')[0]) {
                        await delay(50);
                    }
                }
                while(document.getElementsByClassName('station-item')[0].getElementsByTagName('p')[0].innerText != toCode) {
                    await delay(50);
                }
                document.getElementsByClassName('station-item')[0].getElementsByTagName('div')[0].click();
                await delay(1000);
            }
        }
    }

    // select date
    if(!areDatesEqual(document.querySelector('div[aria-label="Select Your Journey Date"]').innerText, bookingObj.journey_date)) {
        document.querySelector('div[aria-label="Select Your Journey Date"]').click();
        await delay(500);
        clickCalendarDay(bookingObj.journey_date);
    }

    if(bookingObj.quota != 'GN') {
        await delay(500);
        document.querySelector('div[aria-label="Select Your Quota"]').click();
        console.log('Target ID: ');

        await delay(1000);
        const quotaMap = {
          'SS': 'senior',
          'LD': 'ladies',
          'TQ': 'tatkal'
        };

        const targetId = quotaMap[bookingObj.quota];

        if (targetId) {
          document.getElementById(targetId).click();
        }
    }

    if(bookingObj.quota = 'SS') {
        await delay(200);
        var btns = document.getElementsByTagName('button');
        btns[btns.length - 1].click();
        await delay(200);
    } else {
        await delay(1000);
    }

    const buttons = Array.from(document.querySelectorAll('button'));
    const searchBtn = buttons.find(btn => btn.textContent.trim() === 'Search Trains');
    searchBtn.click();
}

function setTimer() {
    console.log('SetTimer Func Start');
    var tqTime = false;

    var quota = bookingObj.quota;
    var travelClass = bookingObj.travel_class;

    // tatkalTime logic
    let tatkalTime = 8;
    if (quota === "TQ" || quota === "PT") {
        tatkalTime = 10;
        if (travelClass === "2S" || travelClass === "FC" || travelClass === "SL") {
            tatkalTime = 11;
        }
    }

    console.log('SetTimer Tatkal time set');

    const now = new Date();
    var cal = new Date(now);

    const refreshTime = bookingObj.refresh_sec + 56; // seconds
    if (refreshTime !== 60) {
        // (tatkalTime - 1):59:refreshTime
        cal.setHours(tatkalTime - 1, 59, refreshTime, 0);
        //cal.setHours(10, 10, 0, 0);
    } else {
        // tatkalTime:00:00
        cal.setHours(tatkalTime, 0, 0, 0);
    }
    cal.setMilliseconds(0);

    console.log('SetTimer Tatkal Time init');

    // testing logic start

      /*cal = new Date(now);
      const seconds = now.getSeconds();

      if (seconds < 25) {
        cal.setSeconds(30);
      } else {
        cal.setSeconds(0);
        cal.setMinutes(cal.getMinutes() + 1);
      }

      console.log('Current Time: ' + now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds());
      console.log('Refresh Time: ' + cal.getHours() + ':' + cal.getMinutes() + ':' + cal.getSeconds());*/

    // testing logic end

    var diffMs = 0;
    try {
        diffMs = cal.getTime() - now.getTime(); // positive if cal is in the future
        console.log('Diff sec: ' + (diffMs/1000));
        if (diffMs > 0 && diffMs <= 360000) { // <= 6 minutes
            if (bookingObj.click_option === 0) {
                tqTime = true;
                console.log('tqtime true');
            }
        }
    } catch (e) {
        // mirror Java's silent catch
    }

    console.log('SetTimer Tqtime set');

    if (tqTime) {
        print('Relax! Availability refresh will start automatically after ' + diffMs/1000 + 's');
        setTimeout(function() {
            startRefresh();
        }, diffMs);
        //startRefreshing();
    }

    console.log('SetTimer Func End');
    return tqTime;
}

async function startRefresh() {
    const generalDiv = Array.from(document.querySelectorAll("div"))
              .find(div => div.textContent.trim() == "General");
    var quotas = generalDiv.parentElement.children;
    var currentIndex = -1;
    var emptyIndex = -1;
    for(i=0;i<quotas.length;i++) {
        if(quotas[i].style.backgroundColor == 'white' && emptyIndex == -1) {
            emptyIndex = i;
        } else if(quotas[i].style.backgroundColor != 'white' && currentIndex == -1) {
            currentIndex = i;
        }
        if(currentIndex != -1 && emptyIndex != -1) {
            break;
        }
    }

    quotas[emptyIndex].click();
    await delay(50);
    quotas[currentIndex].click();

    while(!document.getElementsByClassName('day')[0]) {
        await delay(50);
    }

    while(document.getElementsByClassName('day')[0].children[1].innerText == '') {
        await delay(50);
    }

    var avl = document.getElementsByClassName('day')[0].children[1].innerText;
    if(avl.indexOf('#') == -1) {
        document.getElementById('drawer-footer').getElementsByTagName('button')[0].click();

        await delay(1000);
        if(document.getElementsByClassName('drawer-anim open').length > 1) {
            document.getElementsByClassName('drawer-anim open')[1].getElementsByTagName('button')[1].click();
        }
    } else {
        await delay(750);
        startRefresh();
    }
}

async function autofillTrain() {
    var trainClasses = null;
    var allTrains = document.getElementById('corover-body').getElementsByTagName('div')[0].children[3].children;

    for(i=0; i<allTrains.length; i++) {
        if(allTrains[i].innerHTML.indexOf(bookingObj.train_no) > 0) {
            trainClasses = allTrains[i].getElementsByClassName('tickets')[0].children;
            break;
        }
    }

    for(i=0;i<trainClasses.length;i++) {
        if(trainClasses[i].innerText.indexOf(bookingObj.travel_class) >= 0) {
            trainClasses[i].getElementsByClassName('ticket-new')[0].click();
            break;
        }
    }

    console.log('Waiting for avl');
    while(!document.getElementsByClassName('day')[0]) {
        await delay(50);
    }
    console.log('Avl loaded');

    while(document.getElementsByClassName('day')[0].children[1].innerText == '') {
        await delay(50);
    }
    console.log('Click book now');

    var ret = setTimer();
    if ((!ret && bookingObj.click_option != 1) || (ret && bookingObj.click_option == 2)) {
        document.getElementById('drawer-footer').getElementsByTagName('button')[0].click();

        await delay(1000);
        if(document.getElementsByClassName('drawer-anim open').length > 1) {
            document.getElementsByClassName('drawer-anim open')[1].getElementsByTagName('button')[1].click();
        }
    }
}

async function verifyUserIDPassenger() {
    if(verificationNeeded) {
        await sendRealKeys(document.getElementById('passengers').getElementsByTagName('input')[0], bookingObj.username, {delay: true, tap:true});
        document.getElementById('passengers').getElementsByTagName('button')[0].click();
    }
}

async function autofillPassenger() {
    console.log('askdisha.js Start passenger autofill');
    const ageMap = {
      'M': 'Male',
      'F': 'Female',
      'T': 'Transgender'
    };

    const berthMap = {
      'LB': 'Lower Berth',
      'MB': 'Middle Berth',
      'UB': 'Upper Berth',
      'SL': 'Side Lower',
      'SU': 'Side Upper',
      'CB': 'Cabin',
      'CP': 'Coupe',
      'UB': 'Window Side',
    };

    const foodMap = {
      'V': 'Vegetarian',
      'N': 'Non-Vegetarian',
      'J': 'Jain meal',
      'F': 'Veg (Diabetic)',
      'G': 'Non Veg (Diabetic)',
    };

    await delay(500);
    for(i=0; i<bookingObj.passengers.length; i++) {
        if(!document.getElementById('passengers').getElementsByTagName('input')[0] ||
                document.getElementById('passengers').getElementsByTagName('input')[0].placeholder.toLowerCase().indexOf('name') == -1) {
            console.log('askdisha.js add pass not clicked: ' + document.getElementsByTagName('button')[0].innerText);
            document.getElementsByTagName('button')[0].click();     // add passenger
            await delay(300);
        }

        const genderDiv = Array.from(document.querySelectorAll("div"))
          .find(el => el.textContent.trim() === ageMap[bookingObj.passengers[i].gender]);  // Male, Female, Transgender
        console.log('askdisha.js Gender div: ' + genderDiv.innerText);
        genderDiv.getElementsByTagName('div')[0].click();

        await delay(200);
        await sendRealKeys(document.getElementById('name'), bookingObj.passengers[i].name, {delay: false, tap:true});
        await delay(100);
        await sendRealKeys(document.getElementById('age'), bookingObj.passengers[i].age, {delay: false, tap:true});

        // berth pref
        console.log('askdisha.js Berth pref: ' + bookingObj.passengers[i].berthPref);
        if(bookingObj.passengers[i].berthPref != '') {
            console.log('askdisha.js clicking bp div');
            const bp = Array.from(document.querySelectorAll("div"))
              .find(el => el.textContent.trim() === "Berth Preference");
            bp.getElementsByTagName('div')[0].getElementsByTagName('div')[0].click();

            const pref = Array.from(document.querySelectorAll("div"))
              .find(el => el.textContent.trim() === berthMap[bookingObj.passengers[i].berthPref]);
            if(pref) {
                pref.click();
            } else {
                bp.getElementsByTagName('div')[0].getElementsByTagName('div')[0].click();
            }
        }

        // food pref
        if(bookingObj.passengers[i].meal != 'V' && !bookingObj.no_food) {
            const fp = Array.from(document.querySelectorAll("div"))
              .find(el => el.textContent.trim() === "Vegetarian");
            fp.getElementsByTagName('div')[0].click();

            const pref = Array.from(document.querySelectorAll("div"))
              .find(el => el.textContent.trim() === foodMap[bookingObj.passengers[i].meal]);
            pref.click();
        }

        var btns = document.getElementById('passengers').getElementsByTagName('button');
        btns[btns.length - 1].click();
        await delay(100);

        // check opt berth
        var ageInt = parseInt(bookingObj.passengers[i].age);
        if(ageInt > 5 && ageInt < 11) {
            var allotBerth = Array.from(document.querySelectorAll('p')).filter(p => p.textContent.trim() === 'Allot Berth');
            var abElem = allotBerth[allotBerth.length - 1];
            var abSpan = abElem.parentElement.getElementsByTagName('span')[0];
            var backgroundColor = abSpan.style.backgroundColor;

            // Define our target color
            var checkedColor = 'rgb(42, 65, 157)';

            if (backgroundColor === checkedColor && !bookingObj.passengers[i].optBerth) {   // uncheck it
                abSpan.parentElement.click();
            } else if (backgroundColor != checkedColor && bookingObj.passengers[i].optBerth) {  // check it
                abSpan.parentElement.click();
            }
        }
    }

    for(i=0; i<bookingObj.children.length; i++) {
        if(!document.getElementById('passengers').getElementsByTagName('input')[0] ||
                document.getElementById('passengers').getElementsByTagName('input')[0].placeholder.toLowerCase().indexOf('name') == -1) {
            console.log('askdisha.js add pass not clicked: ' + document.getElementsByTagName('button')[0].innerText);
            document.getElementsByTagName('button')[0].click();     // add passenger
            await delay(300);
        }

        const genderDiv = Array.from(document.querySelectorAll("div"))
          .find(el => el.textContent.trim() === ageMap[bookingObj.children[i].gender]);  // Male, Female, Transgender
        console.log('askdisha.js Gender div: ' + genderDiv.innerText);
        genderDiv.getElementsByTagName('div')[0].click();

        await delay(200);
        await sendRealKeys(document.getElementById('name'), bookingObj.children[i].name, {delay: false, tap:true});
        await delay(100);
        await sendRealKeys(document.getElementById('age'), bookingObj.children[i].age, {delay: false, tap:true});

        var btns = document.getElementById('passengers').getElementsByTagName('button');
        btns[btns.length - 1].click();
        await delay(100);
    }

    // no food
    if(bookingObj.no_food) {
        document.querySelector('[role="switch"]').click();
        await delay(50);
        document.getElementById('passengers').getElementsByTagName('button')[0].click();
    }

    // other pref

    console.log('check other pref');
    if(bookingObj.insurance == 0 || bookingObj.boarding_stn || bookingObj.auto_upgrade || bookingObj.booking_option != 0 || bookingObj.only_confirm) {
        console.log('other pref found');
        const choosePrefDiv = Array.from(document.querySelectorAll("div"))
          .find(div => div.textContent.trim().startsWith("Choose Preference"));
        choosePrefDiv.getElementsByTagName('div')[0].click();
        await delay(500);
        var insElem = document.querySelectorAll('input[type="checkbox"]')[0];
        console.log('other pref clicked. Insurance - ' + bookingObj.insurance + ' | ins elem: ' + insElem);

        if(document.querySelectorAll('input[type="checkbox"]')[0]) {
            if(bookingObj.insurance == 0) {
                console.log('unchecking insurance');
                document.querySelectorAll('input[type="checkbox"]')[0].checked = false;
            } else {
                document.querySelectorAll('input[type="checkbox"]')[0].checked = true;
            }
        }

        console.log('Checking boarding stn - ' + bookingObj.boarding_stn);
        if(bookingObj.boarding_stn) {
            document.getElementsByClassName('boarding')[0].children[1].click();
            console.log('Boarding stn clicked');
            await delay(1000);
            var boardingCode = bookingObj.boarding_stn.split(' - ')[1].trim();

            const allSpans = Array.from(document.getElementsByClassName('drawer-scroll-container')[0].querySelectorAll('span'));
            const targetSpan = allSpans.find(span => span.textContent.trim() === boardingCode);

            if (targetSpan) {
                const clickableDiv = targetSpan.parentElement;
                clickableDiv.click();
                console.log('Selected boarding stn');
                await delay(50);
                document.getElementsByClassName('drawer-scroll-container')[0].getElementsByTagName('button')[0].click();
            }
            /*const svgElement = document.getElementById('drawer-header').getElementsByTagName('svg')[0];

            if (svgElement) {
                const clickEvent = new MouseEvent('click', {
                    view: window,
                    bubbles: true,
                    cancelable: true
                });

                svgElement.dispatchEvent(clickEvent);
                console.log('Close boarding pop up');
            }*/
        }

        if(bookingObj.auto_upgrade) {
            const label = Array.from(document.querySelectorAll('p'))
              .find(p => p.textContent.trim() === 'Consider for Auto Upgrade');

            if (label) {
                const upgradeImg = label.closest('div').parentElement.querySelector('img');

                if (upgradeImg) {
                    upgradeImg.click();
                }
            }
        }

        let targetText = "No Choice"; // Default fallback

        const optionsMap = {
            1: "Book only if confirm in same coach",
            4: "Book only if all the berths are allotted in same coach",
            2: "Book only if confirm and 1 LB",
            3: "Book only if confirm and 2 LB"
        };

        // 1. Determine the target text based on your logic
        if (bookingObj.only_confirm === true) {
            targetText = "Book only if confirm";
        } else {
            // Mapping booking_option to the specific strings in your HTML

            // Set targetText if the option exists in our map
            if (optionsMap[bookingObj.booking_option]) {
                targetText = optionsMap[bookingObj.booking_option];
            }
            console.log('Target text: ' + targetText + ' | Booking opt: ' + bookingObj.booking_option);
        }

        // 2. Find all spans and filter by the target text
        if(targetText != 'No Choice') {
            var targetSpan = Array.from(document.querySelectorAll('span'))
                .find(span => span.textContent.trim() === targetText);

            // 3. Click the parent div (which has the cursor: pointer and the padding)
            if (targetSpan) {
                console.log('Found target span');
                // In your HTML structure, the clickable div is the parent of the span's parent
                // Span (text) -> Span (flex wrapper) -> Div (clickable)
                const clickableDiv = targetSpan.closest('div[style*="cursor: pointer"]');

                if (clickableDiv) {
                    clickableDiv.click();
                }
            } else if(bookingObj.booking_option == 1) {
                console.log('New target text: ' + targetText);
                targetText = optionsMap[4];

                targetSpan = Array.from(document.querySelectorAll('span'))
                    .find(span => span.textContent.trim() === targetText);

                // 3. Click the parent div (which has the cursor: pointer and the padding)
                if (targetSpan) {
                    console.log('Found new target span');
                    // In your HTML structure, the clickable div is the parent of the span's parent
                    // Span (text) -> Span (flex wrapper) -> Div (clickable)
                    const clickableDiv = targetSpan.closest('div[style*="cursor: pointer"]');

                    if (clickableDiv) {
                        clickableDiv.click();
                    }
                }
            }
        }
    }

    console.log('Clicking continue button');
    var btns = document.getElementsByTagName('button');
    console.log('Review: ' + executedFlags.reviewOTP);
    btns[btns.length - 1].click();
}

async function autofillReviewOTP() {
    console.log('Review page');
    var dateTimeNodes = document.getElementsByClassName('date-time');
    var avlNodes = dateTimeNodes[dateTimeNodes.length - 1].children[1].children;
    var avl = avlNodes[avlNodes.length - 1].innerText;
    printAvl(avl);

    // click "Get OTP" button
    var btns = document.getElementsByTagName('button');
    btns[btns.length - 1].click();
}

async function autofillPaymentSelection() {
    console.log('Payment autofill: ' + bookingObj.pymt_autofill);
    console.log('Payment choice: ' + bookingObj.pymt_choice);
    console.log('VPA: ' + bookingObj.vpa);
    if (bookingObj.pymt_autofill) {
        // check fare limit for PT (not needed as of now)

        /*const fareP = Array.from(document.querySelectorAll("p"))
          .find(div => div.textContent.trim().startsWith("₹") && div.textContent.indexOf(",") == -1);
        const fareText = fareP.innerText;
        let fareValue = parseFloat(fareText.match(/[\d,]+\.?\d*//*)[0].replace(/,/g, ''));
        if (bookingObj.fare_limit > 0 && fareValue >= bookingObj.fare_limit && waitForPTResponse !== 2) {
            showFareAlert('₹' + fareValue);
            if (waitForPTResponse === 1 || waitForPTResponse === 3) return;
        }*/

        if(bookingObj.pymt_choice == 'UPI_VPA' || bookingObj.pymt_choice == 'MULTIPLE_GATEWAY') {
            const upiDiv = Array.from(document.querySelectorAll("div"))
                      .find(div => div.textContent.trim() == "UPI");
            upiDiv.click();

            while(!document.getElementsByClassName('upiInput')[0]) {
                await delay(50);
            }
            await delay(100);
            if(bookingObj.qr_payment == 0) {    // upi id
                await sendRealKeys(document.getElementsByClassName('upiInput')[0].getElementsByTagName('input')[0], bookingObj.vpa,
                            {delay: false, tap:true});
                while(!document.getElementsByClassName('input-container')[0].getElementsByTagName('p')[0]) {
                    await delay(50);
                }
                document.getElementsByClassName('button')[0].click();

                while(!document.getElementsByClassName('processing')[0]) {
                    await delay(50);
                }
                openUpiApp();
            } else if(bookingObj.qr_payment == 2) { // upi app
                document.getElementsByClassName('upiButton')[0].click();
            }
        } else if(bookingObj.pymt_choice == 'CREDIT_CARD') {
            const cardsDiv = Array.from(document.querySelectorAll("div"))
                      .find(div => div.textContent.trim().startsWith("Debit"));
            cardsDiv.click();

            while(!document.querySelector('[placeholder="Card Number"')) {
                await delay(50);
            }
            await delay(100);

            await sendRealKeys(document.querySelector('[placeholder="Card Number"'), bookingObj.card_no, {delay: true, tap:true, card: true});
            await sendRealKeys(document.querySelector('[placeholder="MM/YY"'), bookingObj.exp_mon + bookingObj.exp_yr.substring(2, 4), {delay: true, tap:false, expiry: true});
            await sendRealKeys(document.querySelector('[placeholder="CVV"'), bookingObj.cvv, {delay: true, tap:false});

            document.getElementsByClassName('credit-drawer')[0].getElementsByTagName('button')[0].click();

            /*while(!document.querySelectorAll('input[maxlength="1"]')[0]) {
                const shortInputs = document.querySelectorAll('input[maxlength="1"]');

                await sendTap(shortInputs[0]);
                await delay(500);
                if(document.activeElement != shortInputs[0]) {
                    shortInputs[0].focus();
                }
            }*/
        }
    }
}

function jaiShreeRam() {
    try {
        // 1. Login
        if (!executedFlags.login && document.getElementsByClassName('sign-in').length > 0) {
            if(document.getElementsByClassName('sign-in')[0].getElementsByTagName('span')[0].innerText.toLowerCase() == 'sign in') {
                executedFlags.login = true;
                autofillLogin();
            }
        }

        // 1. Journey
        if (!executedFlags.journey && (document.getElementsByClassName('user-greeting')[0] || (document.getElementsByClassName('sign-in')[0] &&
                document.getElementsByClassName('sign-in')[0].getElementsByTagName('span')[0].innerText.toLowerCase() != 'sign in'))) {
            executedFlags.journey = true;
            if(document.getElementsByClassName('user-greeting')[0]) {
                verificationNeeded = true;
            }
            autofillJourney();
        }

        // 2. Train Selection Page
        if (!executedFlags.train && document.getElementsByClassName('indiana-scroll-container')[0]) {
            executedFlags.train = true;
            autofillTrain();
        }

        // 3. Passenger Details Page
        if (document.getElementById('passengers')) {
            if(!executedFlags.verifyUserID && document.getElementById('passengers').getElementsByTagName('input')[0].placeholder.indexOf('IRCTC')) {
                executedFlags.verifyUserID = true;
                verifyUserIDPassenger();
            } else if(!executedFlags.passenger) {
                executedFlags.passenger = true;
                autofillPassenger();
            }
        }

        // 4. Review OTP Page
        const fareTv = Array.from(document.querySelectorAll("span"))
          .find(div => div.textContent.trim() == "View Fare Details");
        if (!executedFlags.reviewOTP && fareTv) {
            console.log('Handling review page');
            executedFlags.reviewOTP = true;
            autofillReviewOTP();
        }

        // 8. Payment Selection Page
        if (!executedFlags.payment && document.getElementById('BookingStepsDiv')) {
            console.log('Payment page found');
            executedFlags.payment = true;
            autofillPaymentSelection();
        }

        /*if(document.getElementsByClassName('UserDetails')[0]) {
            document.getElementsByTagName('img')[0].click();
        }*/
    } catch (e) {
        console.error('[IRCTC Auto]', e);
    }
}

const startedAt = Date.now();

const varInterval = setInterval(() => {
    browser.storage.local.get("bookingData")
        .then((result) => {
            const val = result.bookingData;
            console.log('askdisha.js bookingData: ' + val);

            if (val !== undefined) { // loadingComplete
                loadingComplete = false;
                clearInterval(varInterval);

                // Handle both stringified and plain-object cases
                try {
                    bookingObj = (typeof val === "string") ? JSON.parse(val) : val;
                    pymtChoice = bookingObj.pymt_choice;
                    pymtChoiceLong = bookingObj.pymt_choice_long;
                    bankChoice = bookingObj.bank_choice;
                } catch (e) {
                    console.error("askdisha.js Invalid bookingData JSON:", e, val);
                    return; // bail if unreadable
                }

                console.log("Variables received from storage:", JSON.stringify(bookingObj));

                if (!bookingObj.no_autofill) {
                    var execute = true;
                    if (execute) {
                        // Use MutationObserver to detect SPA route changes
                        const observer = new MutationObserver(() => {
                            jaiShreeRam();
                        });

                        observer.observe(document.body, {
                            childList: true,
                            subtree: true,
                        });

                        // Initial run
                        jaiShreeRam();
                    }
                }
            } else {
                console.log('askdisha.js no bookingData');
                // optional: stop after 10s
                if (Date.now() - startedAt > 10000) {
                    clearInterval(varInterval);
                    console.warn("bookingData not found in storage (timeout)");
                }
            }
        })
        .catch((err) => {
            clearInterval(varInterval);
            console.error("askdisha.js storage.get error:", err);
        });
}, 500);

console.log('askdisha.js logged');
