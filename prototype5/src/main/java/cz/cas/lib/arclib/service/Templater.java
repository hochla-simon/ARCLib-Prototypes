package cz.cas.lib.arclib.service;

import cz.cas.lib.arclib.Utils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Wrapper class around Velocity engine templater.
 */
@Service
public class Templater {
    private VelocityEngine engine;

    public String transform(InputStream template, Map<String, Object> arguments) throws IOException {
        Utils.notNull(template, () -> new IllegalArgumentException("template"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(template, StandardCharsets.UTF_8));
        StringWriter result = new StringWriter();
        VelocityContext velocityContext = new VelocityContext(arguments);

        engine.evaluate(velocityContext, result, "", reader);

        return result.toString();
    }

    @Inject
    public void setEngine(VelocityEngine engine) {
        this.engine = engine;
    }
}
