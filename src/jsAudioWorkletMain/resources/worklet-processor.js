; // make sure kotlin js code last statement is ended

class WorkletProcessor extends AudioWorkletProcessor {

    // Static getter to define AudioParam objects in this custom processor.
    static get parameterDescriptors() {
        return [{
            name: 'volume',
            defaultValue: 0.75
        }];
    }

    constructor() {
        super();

        console.log("worklet-processor.constructor", this, audioWorklet);

        audioWorklet.nl.astraeus.processor.WorkletProcessor.setPort(this.port);

        console.log("STARTED worklet-processor.js");
    }

    process(inputs, outputs, parameters) {
        let result = true;
        let samplesToProcess = 0;

        if (outputs.length !== 1) {
            result = false;
            console.log("Unexpected number of outputs!", outputs)
        } else {
            let channels = outputs[0].length;

            if (channels !== 2) {
                result = false;
                console.log("Unexpected number of channels!", outputs[0]);
            } else if (outputs[0][0].length !== outputs[0][1].length) {
                result = false;
                console.log("Channels have different lengths!!", outputs[0]);
            } else {
                samplesToProcess = outputs[0][0].length;

                audioWorklet.nl.astraeus.processor.WorkletProcessor.process(
                    samplesToProcess,
                    outputs[0][0],
                    outputs[0][1]
                );
            }
        }

        return result;
    }
}

registerProcessor('worklet-processor', WorkletProcessor);
