/* I2P+ RefreshSidebar by dr|z3d */
/* License: AGPLv3 or later */

import { sectionToggler, countNewsItems } from "/js/sectionToggle.js";
import { stickySidebar } from "/js/stickySidebar.js";
import { onVisible, onHidden } from "/js/onVisible.js";
import { refreshInterval, getRefreshTimerId } from "/js/initSidebar.js";

("use strict");

let isDown = false;
let noResponse = 0;
let refreshTimeout;
let refreshTimerActive = true;
let response;
let responseDoc;
let responseText;
const parser = new DOMParser();
const sb = document.querySelector("#sidebar");
const uri = location.pathname;
const worker = new SharedWorker("/js/fetchWorker.js");
const elements = {
  badges: sb.querySelectorAll(".badge:not(#newHosts), #tunnelCount, #newsCount"),
  volatileElements: sb.querySelectorAll(".volatile:not(.badge)"),
};

// Initialize the port and set up message listeners
worker.port.start();
worker.port.addEventListener("message", async function(event) {
  try {
    const { responseText, isDown: workerIsDown, noResponse: workerNoResponse } = event.data;
    if (responseText) {
      responseDoc = parser.parseFromString(responseText, "text/html");
      isDown = workerIsDown;
      noResponse = workerNoResponse;
      document.body.classList.remove("isDown");
      checkIfDown();
    } else {
    }
  } catch (error) {
    noResponse++;
    checkIfDown();
  }
});

async function initSidebar() {
  sectionToggler();
  handleFormSubmit();
  countNewsItems();
  checkTimer();
  checkIfDown();
}

async function checkIfDown() {
  if (noResponse > 5) {
    isDown = true;
    document.body.classList.add("isDown");
  } else {
    document.body.classList.remove("isDown");
  }
}

async function checkTimer() {
  try {
    refreshTimerActive = await getRefreshTimerId();
    return refreshTimerActive.isActive;
  } catch (error) {
    return false;
  }
}

async function doFetch(force = false) {
  checkTimer();
  if (!refreshTimerActive.isActive) {
    return;
  }
  try {
    worker.port.postMessage({ url: `/xhr1.jsp?requestURI=${uri}`, force });
    if (refreshTimeout) {
      clearTimeout(refreshTimeout);
      refreshTimeout = setTimeout(doFetch, refreshInterval);
    }
    noResponse = 0;
    if (force) await refreshSidebar();
  } catch (error) {
    noResponse++;
  }
}

async function refreshSidebar() {
  const xhrContainer = document.getElementById("xhr");
  const updates = [];
  try {
    await doFetch();
    if (refreshTimeout) clearTimeout(refreshTimeout);
    refreshTimeout = setTimeout(refreshSidebar, refreshInterval);
    if (responseDoc) {
      isDown = false;
      const responseElements = {
        volatileElements: responseDoc.querySelectorAll(".volatile:not(.badge)"),
        badges: responseDoc.querySelectorAll(".badge:not(#newHosts)"),
      };
      const updateElement = (elem, respElem, property = "innerHTML") => {
        if (elem && respElem && elem[property] !== respElem[property]) {
          elem[property] = respElem[property];
        }
      };
      const updateIfStatusDown = (elem, respElem) => {
        if (elem && elem.classList.contains("statusDown") && respElem && elem.outerHTML !== respElem.outerHTML) {
          elem.outerHTML = respElem.outerHTML;
        }
      };
      (function checkSections() {
        const updating = xhrContainer.querySelectorAll(".volatile");
        const updatingResponse = responseDoc.querySelectorAll(".volatile");
        if (updating.length !== updatingResponse.length) {
          refreshAll();
        } else {
          updateVolatile();
        }
      })();
      function updateVolatile() {
        Array.from(elements.volatileElements).forEach((elem, index) => {
          const respElem = responseElements.volatileElements[index];
          if (elem && respElem) {
            if (elem.classList.contains("statusDown")) {
              updates.push(() => { updateIfStatusDown(elem, respElem); });
            } else {
              updates.push(() => { updateElement(elem, respElem); });
            }
          }
        });
        if (elements.badges && responseElements.badges) {
          Array.from(elements.badges).forEach((elem, index) => {
            const respElem = responseElements.badges[index];
            updates.push(() => {
              updateElement(elem, respElem, "textContent");
            });
          });
        }
      }
      requestAnimationFrame(() => {
        updates.forEach(update => update());
        updates.length = 0;
        countNewsItems();
        newHosts();
      });
      function refreshAll() {
        if (sb && responseDoc) {
          const sbResponse = responseDoc.getElementById("sb");
          if (sbResponse && sb.innerHTML !== sbResponse.innerHTML) {
            xhrContainer.innerHTML = sbResponse.innerHTML;
          }
          sectionToggler();
        } else {
          noResponse++;
          checkIfDown();
        }
      }
    } else {
      noResponse++;
      checkIfDown();
    }
  } catch (error) {
    noResponse++;
    checkIfDown();
  }
}

function newHosts() {
  const newHostsBadge = document.getElementById("newHosts");
  if (!newHostsBadge) {return;}
  if (theme !== "dark") {
    newHostsBadge.style.display = "none";
    return;
  }

  let lastUpdated = JSON.parse(localStorage.getItem("newHostsData"))?.lastUpdated || 0;
  const period = 300000;
  let now = Date.now();

  if (lastUpdated && (now - lastUpdated) < period) {return;}

  let newHostsInterval;
  function fetchNewHosts() {
    fetch("/susidns/log.jsp").then(response => response.text()).then(html => {
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, "text/html");
      const now = new Date();
      const oneDayAgo = new Date(now.getTime() - 24 * 60 * 60 * 1000);
      const entries = Array.from(doc.querySelectorAll("li"));
      const recentEntries = entries.filter(entry => {
        const dateText = entry.querySelector(".date").textContent;
        const entryDate = new Date(dateText);
        return entryDate >= oneDayAgo;
      });

      const newHostnames = recentEntries.flatMap(entry => {
        const dateText = entry.querySelector(".date").textContent;
        const entryDate = new Date(dateText);
        const links = entry.querySelectorAll("a");
        return Array.from(links).map(a => ({
          hostname: new URL(a.href).hostname,
          timestamp: entryDate.getTime()
        }));
      });

      const storedData = JSON.parse(localStorage.getItem("newHostsData") || "{}");
      const storedHostnames = storedData.hostnames || [];

      // Combine new and stored hostnames
      const allHostnames = [...new Set([...newHostnames, ...storedHostnames].map(h => h.hostname))]
        .map(hostname => {
          const newEntry = newHostnames.find(h => h.hostname === hostname);
          const storedEntry = storedHostnames.find(h => h.hostname === hostname);
          return newEntry || storedEntry;
        });

      // Filter out old hostnames
      const recentHostnames = allHostnames.filter(hostname => {
        const timestamp = new Date(hostname.timestamp);
        return timestamp >= oneDayAgo;
      });

      recentHostnames.sort((a, b) => b.timestamp - a.timestamp); // Sort by timestamp (newest first)
      const limitedHostnames = recentHostnames.slice(0, 10); // Keep only the 10 most recent (added in last 24h)
      const sortedHostnames = limitedHostnames.map(item => item.hostname).sort(); // Sort alphabetically
      const count = sortedHostnames.length;
      localStorage.setItem("newHostsData", JSON.stringify({ count, lastUpdated: Date.now(), hostnames: limitedHostnames }));

      if (count > 0) {
        if (count > 10) { newHostsBadge.textContent = "10+"; }
        else { newHostsBadge.textContent = count; }
        newHostsBadge.hidden = false;
      } else { newHostsBadge.hidden = true; }
      if (sortedHostnames !== null) { updateTooltip(sortedHostnames); }
    }).catch(() => {});
  }

  function getNewHosts() {
    if (!newHostsBadge) { return; }
    const now = Date.now();
    const storedData = JSON.parse(localStorage.getItem("newHostsData") || "{}");
    const { count, lastUpdated, hostnames } = storedData;
    if (lastUpdated && count && (now - lastUpdated < 60000)) {
      if (count > 0) {
        if (count > 10) { newHostsBadge.textContent = "10+"; }
        else { newHostsBadge.textContent = count; }
        newHostsBadge.hidden = false;
        updateTooltip(hostnames.map(h => h.hostname));
      } else { newHostsBadge.hidden = true; }
    } else { fetchNewHosts(); }
  }

  function updateTooltip(hostnames) {
    if (!newHostsBadge) {return;}
    const services = document.getElementById("sb_services");
    const servicesTd = services?.querySelector("tr:first-child td");
    const newHosts = document.getElementById("newHostsList");
    const newHostsTd = newHosts?.querySelector("td");
    const newHostsList = hostnames.map(hostname => `<a href="http://${hostname}" target=_blank>${hostname.replace(".i2p", "")}</a>`).join("");
    newHosts.hidden = true;

    if (hostnames.length > 0) { newHostsTd.innerHTML = newHostsList; }
    newHostsBadge?.addEventListener("mouseenter", () => {
      newHosts.hidden = false;
      services.classList.add("tooltipped");
    }, {passive: true});
    services?.addEventListener("mouseleave", () => {
      newHosts.hidden = true;
      services.classList.remove("tooltipped");
    }, {passive: true});
  }

  if (newHostsInterval) { clearInterval(newHostsInterval); }
  getNewHosts();
  lastUpdated = now;
  newHostsInterval = setInterval(fetchNewHosts, period);
}

async function handleFormSubmit() {
  document.addEventListener("submit", async (event) => {
    const form = event.target.closest("form");
    const clickTarget = event.submitter;
    if (!form || !clickTarget) { return; }
    await doFetch(true);
    const formId = form.getAttribute("id");
    const hiddenIframe = document.getElementById("processSidebarForm");
    hiddenIframe.removeEventListener("load", handleLoad);
    hiddenIframe.addEventListener("load", handleLoad);
    form.dispatchEvent(new Event("submit"));
    function handleLoad(event) {
      const formResponse = responseDoc.querySelector(`#${formId}`);
      if (!formResponse) return;
      if (form.id !== "form_sidebar") {
        form.innerHTML = formResponse.innerHTML;
        form.classList.add("activated");
        const shutdownNotice = document.getElementById("sb_shutdownStatus");
        const shutdownNoticeHR = sb.querySelector("#sb_shutdownStatus+hr");
        const shutdownNoticeResponse = responseDoc.getElementById("sb_shutdownStatus");
        const updateForm = document.getElementById("sb_updateform");
        const updateFormResponse = responseDoc.getElementById("sb_updateform");
        if (shutdownNotice) {
          if (shutdownNoticeResponse && shutdownNoticeResponse.classList.contains("inactive")) {
            shutdownNotice.hidden = true;
            shutdownNoticeHR.hidden = true;
          } else if (shutdownNoticeResponse && shutdownNotice.innerHTML !== shutdownNoticeResponse.innerHTML) {
            shutdownNotice.hidden = false;
            shutdownNoticeHR.hidden = false;
            shutdownNotice.outerHTML = shutdownNoticeResponse.outerHTML;
          }
        }
        if (updateForm && updateFormResponse && updateFormResponse.classList.contains("inactive")) {
          updateForm.hidden = true;
        } else if (updateForm && updateFormResponse && updateForm.innerHTML !== updateFormResponse.innerHTML) {
          updateForm.outerHTML = updateFormResponse.outerHTML;
        }
        if (form.id === "sb_routerControl") {
          const tunnelStatus = document.getElementById("sb_tunnelstatus");
          const tunnelStatusResponse = responseDoc.getElementById("sb_tunnelstatus");
          if (tunnelStatusResponse && tunnelStatus.innerHTML !== tunnelStatusResponse.innerHTML) {
            tunnelStatus.outerHTML = tunnelStatusResponse.outerHTML;
          }
        }
        const buttons = form.querySelectorAll("button");
        if (buttons.length > 0) {
          buttons.forEach(button => {
            button.style.opacity = ".5";
            button.style.pointerEvents = "none";
          });
        }
      } else {refreshAll();}
    }
    refreshSidebar();
  });
}

const ready = async () => {
  try {
    checkIfDown();
    await doFetch();
    await refreshSidebar();
    noResponse = 0;
  } catch (error) {}
}

onVisible(sb, ready);
document.addEventListener("DOMContentLoaded", initSidebar);
document.addEventListener("DOMContentLoaded", newHosts);

export {refreshSidebar};