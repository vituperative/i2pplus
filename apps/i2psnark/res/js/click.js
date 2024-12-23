/* I2P+ click.js for I2PSnark by dr|z3d */
/* Simulate longer button clicks by adding .depress class to inputs */
/* License: AGPL3 or later */

import {refreshScreenLog} from "./refreshTorrents.js";

let eventListenerActive = false;

document.addEventListener("DOMContentLoaded", () => {
  if (eventListenerActive) {return;}

  const page = document.getElementById("page") || document.querySelector(".page");

  const handleInputClick = async (clickTarget) => {
    const clickable = ".toggleview, .snarkNav, .filter, input[class^='action'], input.add, input.create";
    const targetElement = clickTarget.matches(clickable) ? clickTarget : clickTarget.closest(clickable);
    if (!targetElement) {return;}
    let delay = 360;
    const isAction = targetElement.matches("input[class^='action'], input[id^='action']");
    const isFormButton = targetElement.matches("input[type=submit]");
    const currentForm = targetElement.closest("form");
    const isUIElement = targetElement.closest(".toggleview, .snarkNav, .filter");

    targetElement.classList.add("depress");

    if (isAction) {
      const iframe = document.getElementById("processForm");
      if (iframe) {
        const formTarget = targetElement.form.target;
        if (formTarget === "processForm" && isAction) {delay = 4000;}
      }
      const nonClickedActionButtons = currentForm.querySelectorAll("input[type=submit][class^='action']:not(.depress), input[type=submit][id^='action']:not(.depress)");
      nonClickedActionButtons.forEach((el) => el.classList.add("tempDisabled"));
      currentForm.onsubmit = async (event) => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        await refreshScreenLog(undefined, true);
        await new Promise(resolve => setTimeout(resolve, 3000));
        targetElement.classList.replace("depress", "inert");
        nonClickedActionButtons.forEach((input) => input.classList.remove("tempDisabled"));
      };
    } else if (isFormButton) {
      setTimeout(async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        await refreshScreenLog(undefined, true);
        clickTarget.classList.replace("depress", "inert");
      }, delay);
    } else {
      setTimeout(async() => { clickTarget.classList.replace("depress", "inert"); }, delay);
    }
    setTimeout(async () => {
      targetElement.classList.remove("depress");
      targetElement.classList.remove("inert");
    }, delay);
  };

  page.addEventListener("click", (event) => {
    const clickTarget = event.target;
    if ((clickTarget.matches("input.add") || clickTarget.matches("input.create"))) {
      event.preventDefault();
      event.stopPropagation();
      clickTarget.form.requestSubmit();
      handleInputClick(clickTarget);
    } else {handleInputClick(clickTarget);}
    eventListenerActive = true;
  });

});