import { Logger, LogLevel, LogOutput } from './logger.service';

type LogMethod = 'log' | 'info' | 'warn' | 'error';
const logMethods: LogMethod[] = ['log', 'info', 'warn', 'error'];

describe('Logger', () => {
  let savedConsole: Record<LogMethod, (...args: any[]) => void>;
  let savedLevel: LogLevel;
  let savedOutputs: LogOutput[];

  beforeAll(() => {
    savedConsole = {
      log: console.log,
      info: console.info,
      warn: console.warn,
      error: console.error,
    };

    const patchedConsole = console as unknown as Record<LogMethod, (...args: any[]) => void>;
    logMethods.forEach((m) => {
      patchedConsole[m] = () => {};
    });

    savedLevel = Logger.level;
    savedOutputs = Logger.outputs;
  });

  beforeEach(() => {
    Logger.level = LogLevel.Debug;
  });

  afterAll(() => {
    const patchedConsole = console as unknown as Record<LogMethod, (...args: any[]) => void>;
    logMethods.forEach((m) => {
      patchedConsole[m] = savedConsole[m];
    });
    Logger.level = savedLevel;
    Logger.outputs = savedOutputs;
  });

  it('should create an instance', () => {
    expect(new Logger()).toBeTruthy();
  });

  it('should add a new LogOutput and receives log entries', () => {
    // Arrange
    const calls: Array<{ source: string | undefined; level: LogLevel; args: any[] }> = [];
    const outputSpy: LogOutput = (source, level, ...args) => {
      calls.push({ source, level, args });
    };
    const log = new Logger('test');

    // Act
    Logger.outputs.push(outputSpy);

    log.debug('d');
    log.info('i');
    log.warn('w');
    log.error('e', { error: true });

    // Assert
    expect(calls.length).toBe(4);
    expect(calls[0]).toEqual({ source: 'test', level: LogLevel.Debug, args: ['d'] });
    expect(calls[1]).toEqual({ source: 'test', level: LogLevel.Info, args: ['i'] });
    expect(calls[2]).toEqual({ source: 'test', level: LogLevel.Warning, args: ['w'] });
    expect(calls[3]).toEqual({ source: 'test', level: LogLevel.Error, args: ['e', { error: true }] });
  });

  it('should add a new LogOutput and receives only production log entries', () => {
    // Arrange
    const calls: Array<{ source: string | undefined; level: LogLevel; args: any[] }> = [];
    const outputSpy: LogOutput = (source, level, ...args) => {
      calls.push({ source, level, args });
    };
    const log = new Logger('test');

    // Act
    Logger.outputs.push(outputSpy);
    Logger.enableProductionMode();

    log.debug('d');
    log.info('i');
    log.warn('w');
    log.error('e', { error: true });

    // Assert
    expect(calls.length).toBe(2);
    expect(calls[0]).toEqual({ source: 'test', level: LogLevel.Warning, args: ['w'] });
    expect(calls[1]).toEqual({ source: 'test', level: LogLevel.Error, args: ['e', { error: true }] });
  });
});
