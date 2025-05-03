$(document).ready(function () {
    const $inputNumber = $("#inputNumber");
    const $inputBase = $("#inputBase");
    const $customBase = $("#customBase");

    function updateResults() {
        let numberStr = $inputNumber.val().trim();
        let inputBase = $inputBase.val().trim();
        let customBase = $customBase.val().trim();

        if (numberStr === "" || isNaN(inputBase) || inputBase < 2 || inputBase > 64) return;

        inputBase = parseInt(inputBase);
        let decimalNumber = fromCustomBase(numberStr, inputBase); // Returns a string
        if (!decimalNumber || decimalNumber === "NaN") return; // Prevent invalid inputs

        // Validate custom base; default to 10 if invalid
        customBase = (customBase !== "" && !isNaN(customBase) && customBase >= 2 && customBase <= 64) ? parseInt(customBase) : 10;

        // Prepare the BigIntegerRequest DTO
        const requestData = {
            number: decimalNumber, // Send as string
            customBase: customBase
        };

        $.ajax({
            url: "/converter/integer-base-converter",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            success: function (data) {
                console.log("Server response:", data); // Debugging log
                $("#resultBinary").val(data["2"] || "");
                $("#resultOctal").val(data["8"] || "");
                $("#resultDecimal").val(data["10"] || "");
                $("#resultHexadecimal").val(data["16"] || "");
                $("#resultBase64").val(data["64"] || "");
                $("#resultCustom").val(data[customBase] || "");

                // Display the custom base error if it exists
                if (data["customBaseError"]) {
                    $("#customBaseError").text(data["customBaseError"]).show();
                } else {
                    $("#customBaseError").hide();
                }
            },
            error: function () {
                console.error("Error fetching conversion results");
                $("#customBaseError").text("Invalid custom base").show();
            }
        });
    }

    function copyToClipboard(elementId) {
        let input = $(elementId);
        if (input.length) {
            navigator.clipboard.writeText(input.val());
        }
    }

    // Custom function to convert any base (2–64) to decimal using BigInt
    const BASE_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/";

    function fromCustomBase(numberStr, base) {
        if (base < 2 || base > 64) return NaN; // Invalid base
        numberStr = numberStr.trim();

        let decimalValue = BigInt(0);
        let bigBase = BigInt(base);

        for (let i = 0; i < numberStr.length; i++) {
            let digitValue = BASE_ALPHABET.indexOf(numberStr[i]);
            if (digitValue === -1 || digitValue >= base) return NaN; // Invalid character for base

            decimalValue = decimalValue * bigBase + BigInt(digitValue);
        }
        return decimalValue.toString(); // Return as string to prevent rounding issues
    }

    // Debounce function to prevent excessive API calls
    function debounce(func, delay = 100) {
        let timer;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => func.apply(this, args), delay);
        };
    }

    // Attach debounced event listeners
    $inputNumber.on("input", debounce(updateResults));
    $inputBase.on("input", debounce(updateResults));
    $customBase.on("input", debounce(updateResults));

    // Expose function globally for clipboard buttons
    window.copyToClipboard = copyToClipboard;
});