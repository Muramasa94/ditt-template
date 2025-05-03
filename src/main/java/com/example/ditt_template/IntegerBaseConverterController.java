package com.example.ditt_template;

import java.util.List;
import java.util.Map;

import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ditt_sdk.Service.DevToolController;
import com.example.ditt_sdk.Service.DevToolExtensionPoint;

@Extension
@Controller
@RequestMapping("/converter")
@DevToolController(
    name = "Integer Base Converter",
    category = "Converter",
    description = "Convert a number between different bases (decimal, hexadecimal, binary, octal, base64, ...)",
    isPremium = 0,
    isEnabled = 1,
    pluginFolder = "integer-base-converter"
)
public class IntegerBaseConverterController implements DevToolExtensionPoint {

    @Autowired
    private IntegerBaseConverter integerBaseConverter;

    @GetMapping("/integer-base-converter")
    public String loadPage(Model model) {
        List<String> customCSS = List.of("color.css", "style.css", "integer-base-converter.css");
        List<String> customJS = List.of("intBaseConverter.js");
        model.addAttribute("customCSS", customCSS);
        model.addAttribute("customJS", customJS);
        return "converter-integer-base-converter";
    }

    @PostMapping("/integer-base-converter")
    @ResponseBody
    public Map<String, String> handleRequest(@RequestBody BigIntegerRequest request) {
        return integerBaseConverter.execute(request);
    }
}