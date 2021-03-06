package it.unipi.dii.inginf.dmml.soundhabit.classification;

import it.unipi.dii.inginf.dmml.soundhabit.config.ConfigurationParameters;
import it.unipi.dii.inginf.dmml.soundhabit.utils.Utils;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.List;

public class SongFeature {
    private double chromaStft;
    private double rms;
    private double spectralCentroid;
    private double spectralBandwidth;
    private double spectralRolloff;
    private double zeroCrossingRate;
    private List<Double> mfcc;

    public SongFeature(double chromaStft, double rms, double spectralCentroid, double spectralBandwidth,
                       double spectralRolloff, double zeroCrossingRate, List<Double> mfcc) {
        this.chromaStft = chromaStft;
        this.rms = rms;
        this.spectralCentroid = spectralCentroid;
        this.spectralBandwidth = spectralBandwidth;
        this.spectralRolloff = spectralRolloff;
        this.zeroCrossingRate = zeroCrossingRate;
        this.mfcc = mfcc;
    }

    public SongFeature () {}

    /**
     * Function that transform the SongFeature in a Instance
     * @return a dataset containing the new song
     */
    public Instances toInstances () {
        Instances instances = null;
        try {
            instances = Utils.loadDataset(ConfigurationParameters.getInstance().getDatasetPath());

            double[] values = new double[27];
            values[0] = chromaStft;
            values[1] = rms;
            values[2] = spectralCentroid;
            values[3] = spectralBandwidth;
            values[4] = spectralRolloff;
            values[5] = zeroCrossingRate;
            int i=6;
            for (Double m : mfcc)
            {
                values[i] = m;
                i++;
            }
            Instance instance = new DenseInstance(1.0, values);
            instance.setDataset(instances);
            instance.setClassMissing();

            instances.delete();
            instances.add(instance);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instances;
    }

    public double getChromaStft() {
        return chromaStft;
    }

    public void setChromaStft(double chromaStft) {
        this.chromaStft = chromaStft;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public double getSpectralCentroid() {
        return spectralCentroid;
    }

    public void setSpectralCentroid(double spectralCentroid) {
        this.spectralCentroid = spectralCentroid;
    }

    public double getSpectralBandwidth() {
        return spectralBandwidth;
    }

    public void setSpectralBandwidth(double spectralBandwidth) {
        this.spectralBandwidth = spectralBandwidth;
    }

    public double getSpectralRolloff() {
        return spectralRolloff;
    }

    public void setSpectralRolloff(double spectralRolloff) {
        this.spectralRolloff = spectralRolloff;
    }

    public double getZeroCrossingRate() {
        return zeroCrossingRate;
    }

    public void setZeroCrossingRate(double zeroCrossingRate) {
        this.zeroCrossingRate = zeroCrossingRate;
    }

    public List<Double> getMfcc() {
        return mfcc;
    }

    public void setMfcc(List<Double> mfcc) {
        this.mfcc = mfcc;
    }

    @Override
    public String toString() {
        return "SongFeature{" +
                "chromaStft=" + chromaStft +
                ", rms=" + rms +
                ", spectralCentroid=" + spectralCentroid +
                ", spectralBandwidth=" + spectralBandwidth +
                ", spectralRolloff=" + spectralRolloff +
                ", zeroCrossingRate=" + zeroCrossingRate +
                ", mfcc=" + mfcc +
                '}';
    }
}
