"use client";

import dynamic from "next/dynamic";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ArrowRight, Sparkles } from "lucide-react";

const OrbitalSphere = dynamic(
  () => import("./orbitalsphere").then((m) => m.OrbitalSphere),
  { ssr: false, loading: () => <div className="w-full h-full" /> },
);

export function HeroSection() {
  const chips: {
    label: string;
    delay: string;
    top?: string;
    bottom?: string;
    left?: string;
    right?: string;
  }[] = [
    { label: "Branches", top: "10%", left: "4%", delay: "0.8s" },
    { label: "Analytics AI", top: "18%", right: "2%", delay: "1s" },
    { label: "Inventory", bottom: "26%", left: "2%", delay: "0.9s" },
    { label: "Sales CRM", bottom: "18%", right: "4%", delay: "1.1s" },
    { label: "Warehouses", top: "50%", left: "0%", delay: "1.2s" },
  ];
  return (
    <section className="relative min-h-screen flex items-center overflow-hidden bg-background">
      {/* Subtle dot-grid background */}
      <div
        className="absolute inset-0 opacity-[0.03] pointer-events-none"
        style={{
          backgroundImage: `radial-gradient(hsl(var(--foreground)) 1px, transparent 1px)`,
          backgroundSize: "32px 32px",
        }}
      />
      {/* Soft vignette */}
      <div
        className="absolute inset-0 pointer-events-none"
        style={{
          background:
            "radial-gradient(ellipse at center, transparent 40%, hsl(var(--background)) 85%)",
        }}
      />

      <div className="relative z-10 max-w-7xl mx-auto px-6 w-full pt-28 pb-16">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          {/* Left — text */}
          <div className="flex flex-col gap-6">
            <Badge
              variant="outline"
              className="w-fit gap-1.5 px-3 py-1.5 text-xs font-medium text-muted-foreground animate-fade-in"
              style={{ animationDelay: "0.1s" }}
            >
              <Sparkles size={11} />
              Multi-tenant business infrastructure
            </Badge>

            <h1
              className="text-5xl md:text-6xl xl:text-7xl font-bold leading-[1.07] tracking-tight animate-slide-up"
              style={{ animationDelay: "0.2s" }}
            >
              <span className="text-foreground">One sphere.</span>
              <br />
              <span className="text-foreground">Every business</span>
              <br />
              <span className="text-muted-foreground">operation.</span>
            </h1>

            <p
              className="text-muted-foreground text-lg leading-relaxed max-w-[480px] animate-slide-up"
              style={{ animationDelay: "0.35s" }}
            >
              NexusSphere gives every business its own isolated workspace — with
              branches, inventory, sales, shipments, AI analytics and more — all
              governed by role-based access control.
            </p>

            <div
              className="flex flex-wrap items-center gap-4 animate-slide-up"
              style={{ animationDelay: "0.5s" }}
            >
              <Link href="/auth">
                <Button
                  size="lg"
                  className="gap-2 text-base px-7 hover:scale-[1.02] transition-transform"
                >
                  Launch your workspace
                  <ArrowRight size={16} />
                </Button>
              </Link>
              <a href="#how-it-works">
                <Button size="lg" variant="outline" className="text-base px-7">
                  See how it works
                </Button>
              </a>
            </div>

            {/* Stats */}
            <div
              className="flex flex-wrap gap-8 pt-2 border-t border-border animate-fade-in"
              style={{ animationDelay: "0.65s" }}
            >
              {[
                { value: "9+", label: "Business services" },
                { value: "∞", label: "Workspaces" },
                { value: "RBAC", label: "Built-in access control" },
              ].map((stat) => (
                <div key={stat.label} className="flex flex-col pt-4">
                  <span className="text-2xl font-bold text-foreground">
                    {stat.value}
                  </span>
                  <span className="text-xs text-muted-foreground mt-0.5">
                    {stat.label}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Right — 3D */}
          <div
            className="relative h-[460px] lg:h-[580px] animate-fade-in"
            style={{ animationDelay: "0.3s" }}
          >
            <OrbitalSphere />

            {/* Floating service chips */}
            {chips.map((chip) => (
              <div
                key={chip.label}
                className="absolute px-3 py-1.5 rounded-full border border-border bg-background/80 backdrop-blur-sm text-muted-foreground text-[11px] font-medium whitespace-nowrap animate-fade-in pointer-events-none shadow-sm"
                style={{
                  top: chip.top,
                  left: chip.left,
                  right: chip.right,
                  bottom: chip.bottom,
                  animationDelay: chip.delay,
                }}
              >
                <span className="mr-1.5 text-foreground opacity-40">◆</span>
                {chip.label}
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Scroll cue */}
      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 flex flex-col items-center gap-1 opacity-30">
        <div className="w-px h-10 bg-gradient-to-b from-transparent to-foreground animate-pulse" />
        <span className="text-[10px] text-muted-foreground tracking-widest uppercase">
          scroll
        </span>
      </div>
    </section>
  );
}
